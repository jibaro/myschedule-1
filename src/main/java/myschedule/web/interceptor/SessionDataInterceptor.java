package myschedule.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import myschedule.service.SchedulerServiceContainer;
import myschedule.service.SchedulerServiceFinder;
import myschedule.web.SessionData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionDataInterceptor extends HandlerInterceptorAdapter {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired @Qualifier("schedulerServiceFinder")
	protected SchedulerServiceFinder schedulerServiceFinder;
	
	@Autowired @Qualifier("schedulerServiceContainer")
	protected SchedulerServiceContainer schedulerServiceContainer;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Ensure session data is created.
		HttpSession session = request.getSession(true); // create session if not exists.
		SessionData sessionData = schedulerServiceFinder.getOrCreateSessionData(session);
		if (sessionData.getCurrentSchedulerName() == null) {
			List<String> names = schedulerServiceContainer.getSchedulerServiceNames();
			if (names.size()  > 0) {
				sessionData.setCurrentSchedulerName(names.get(0)); // Use first one in list.
			} else {
				logger.info("No scheduler service found in the application!");
			}
		}
		return true; // continue process.
	}
}
