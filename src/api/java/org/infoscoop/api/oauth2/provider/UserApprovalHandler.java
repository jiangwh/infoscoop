/*
 *  infoScoop OpenSource
 * Copyright (C) 2010 Beacon IT Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 */

package org.infoscoop.api.oauth2.provider;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.TokenServicesUserApprovalHandler;

public class UserApprovalHandler extends TokenServicesUserApprovalHandler {

	private Collection<String> autoApproveClients = new HashSet<String>();
	
	private boolean useTokenServices = true;

	/**
	 * @param useTokenServices the useTokenServices to set
	 */
	public void setUseTokenServices(boolean useTokenServices) {
		this.useTokenServices = useTokenServices;
	}

	/**
	 * @param autoApproveClients the auto approve clients to set
	 */
	public void setAutoApproveClients(Collection<String> autoApproveClients) {
		this.autoApproveClients = autoApproveClients;
	}

	/**
	 * Allows automatic approval for a white list of clients in the implicit grant case.
	 * 
	 * @param authorizationRequest The authorization request.
	 * @param userAuthentication the current user authentication
	 * 
	 * @return Whether the specified request has been approved by the current user.
	 */
	@Override
	public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
	
		// If we are allowed to check existing approvals this will short circuit the decision
		if (useTokenServices && super.isApproved(authorizationRequest, userAuthentication)) {
			return true;
		}

		if (!userAuthentication.isAuthenticated()) {
			return false;
		}

		String flag = authorizationRequest.getApprovalParameters().get(AuthorizationRequest.USER_OAUTH_APPROVAL);
		boolean approved = flag != null && flag.toLowerCase().equals("true");

		return approved
				|| (authorizationRequest.getResponseTypes().contains("token") && autoApproveClients
						.contains(authorizationRequest.getClientId()));

	}

}