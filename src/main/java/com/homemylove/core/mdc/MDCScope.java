package com.homemylove.core.mdc;


import com.homemylove.auth.AuthInfo;
import org.slf4j.MDC;

public class MDCScope {
    public static void saveAuthInfo(AuthInfo authInfo){
        try{
            MDC.put(MDCKey.ID,authInfo.getId().toString());
            MDC.put(MDCKey.USERNAME,authInfo.getUsername());
            MDC.put(MDCKey.AVATAR,authInfo.getAvatar());
        }catch (Exception e){
        }
    }

    public static AuthInfo getAuthInfo(){
        String id = MDC.get(MDCKey.ID);
        if(id == null) return null;
        AuthInfo authInfo = new AuthInfo();
        authInfo.setId(Long.valueOf(id));
        String username = MDC.get(MDCKey.USERNAME);
        authInfo.setUsername(username);
        String avatar = MDC.get(MDCKey.AVATAR);
        authInfo.setAvatar(avatar);
        return authInfo;
    }

    public static void removeAuthInfo() {
        MDC.remove(MDCKey.ID);
        MDC.remove(MDCKey.USERNAME);
        MDC.remove(MDCKey.AVATAR);
    }
}
