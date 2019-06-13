package ewhine.app.http.module;



public interface IUser {

	public String getName();

	public boolean isValidPassword(String password);

	public boolean isUserInRole(String scope, String role_name);

	public UserIdentity getUserIdentity();

	public void logout();

}
