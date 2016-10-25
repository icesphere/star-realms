package org.smartreaction.starrealms.view;

import org.apache.commons.codec.digest.DigestUtils;
import org.smartreaction.starrealms.service.LoggedInUsers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class LoginView implements Serializable {
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @EJB
    LoggedInUsers loggedInUsers;

    private String username;

    private String betaCode;

    private boolean showUsernameError;

    private boolean showBetaCodeError;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String loginAsGuest() {
        showBetaCodeError = false;
        showUsernameError = false;

        if (!validBetaCode()) {
            showBetaCodeError = true;
            return null;
        } else if (userSession.loginAsGuest(username, betaCode)) {
            showUsernameError = false;
            return "lobby.xhtml?faces-redirect=true";
        } else {
            showUsernameError = true;
            return null;
        }
    }

    public boolean validBetaCode() {
        List<String> validDigests = new ArrayList<>();
        validDigests.add("2fdd9c1c8ca82cf49d3a79edfcb2cce6");
        validDigests.add("4f88e9f49dd42230da751fe27b7d3452");
        validDigests.add("57e134e563ff8073989545935b393f58");
        validDigests.add("a6b7fa76c366274b20dbcddf40946f34");
        validDigests.add("ad7cdb4636ecd10b97a9786841afda05");
        validDigests.add("1b1b7ae9d0e7cb8d774823ed0830f9e7");
        validDigests.add("9d6bbfc2ac61b3bc91fee4e7b4530ef5");
        validDigests.add("2313de3b4982352df161a1ca2d60608f");
        validDigests.add("eed7612a7988118735f3191f3d1186a2");
        validDigests.add("c69f8c89b79cd40c7caa3cef432e4ec4");
        validDigests.add("0f4981d1ea5adab19081057331fb1bbf");
        validDigests.add("b6696507cb7d0aab41247546572d65c6");
        validDigests.add("d6257a3dc1aa18878e929174f8e544cf");

        String digest = DigestUtils.md5Hex("srsalt" + betaCode);

        return validDigests.contains(digest);
    }

    public boolean isShowUsernameError() {
        return showUsernameError;
    }

    public boolean isShowBetaCodeError() {
        return showBetaCodeError;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public String getBetaCode() {
        return betaCode;
    }

    public void setBetaCode(String betaCode) {
        this.betaCode = betaCode;
    }
}
