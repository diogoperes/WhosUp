package com.whosup.android.whosup.utils;


public class InviteAttend {

    private String id;
    private String idInvite;
    private String hostUsername;
    private String invitedUsername;
    private String state;
    private User userProfile;


    public InviteAttend(String id, String idInvite, String hostUsername, String invitedUsername, String state, User userProfile) {
        super();
        this.id = id;
        this.idInvite = idInvite;
        this.hostUsername = hostUsername;
        this.invitedUsername = invitedUsername;
        this.state = state;
        this.userProfile = userProfile;

    }


}
