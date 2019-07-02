package com.codecamos.timetracking.config;


import com.codecamos.timetracking.model.ApiError;

public final class Resource {

	private Resource() { }

	/**
	 * @param activeProfiles The current active profiles this application was started with, {@link Profile} for available profiles
	 * @return returns true if we are in production
	 */
	public static Boolean isInDevOrStage(final String[] activeProfiles) {
		for (String profile : activeProfiles) {
			if (profile.equals(Profile.DEVELOPMENT) || profile.equals(Profile.STAGE)) {
				return true;
			}
		}
		return false;
	}

	public static class Profile {
		public static final String DEVELOPMENT = "dev";
		public static final String PRODUCTION = "prod";
		public static final String STAGE = "stage";
	}

	/**
	 * This class holds are the error codes present in the application and is used
	 * in conjuction with a {@link BaseException}
	 *
	 */
	public static class ErrorCode {
		/**
		* Generic error code, use as little as possible
		*/
		public static final int GENERIC = 9999;
		/**
		 * Code used when access for a certain endpoint or service is not permitted
		 */
		public static final int ACCESS_FORBIDDEN = 9998;

		/**
		 * Code used for accessing an endpoint that does not exist, is missing or is forbidden
		 */
		public static final int FORBIDDEN_MISSING_OR_MOVE = 9997;
		/**
		 * The resource you are querying can not be found
		 */
		public static final int RESOURCE_NOT_FOUND = 9996;
	}

	public static final ApiError GENERIC_ERROR = new ApiError(ErrorCode.GENERIC, "Generic error message");
	public static final ApiError ACCESS_ERROR = new ApiError(ErrorCode.ACCESS_FORBIDDEN, "You do not have permission to access this endpoint");
}
