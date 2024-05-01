package com.butter.wypl.sidetab.service;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.sidetab.data.response.GoalWidgetResponse;

public interface SideTabLoadService {

	GoalWidgetResponse findGoal(final AuthMember authMember, final int sideTabId);
}
