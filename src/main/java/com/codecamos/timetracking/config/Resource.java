package com.codecamos.timetracking.config;


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
		public static final long GENERIC_CODE = 9999;
	}
}
