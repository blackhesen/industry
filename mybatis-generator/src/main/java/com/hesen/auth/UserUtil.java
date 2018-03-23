package com.hesen.auth;

/**
 * Created by LeonWang on 2015/8/26.
 */
public class UserUtil {
    private static final ThreadLocal THREAD_LOCAL=new ThreadLocal();
    private UserUtil(){}

    private static ThreadLocal initialThreadLocal (){
        if(THREAD_LOCAL!=null) {
            return THREAD_LOCAL;
        } else {
            return new ThreadLocal();
        }
    }

    public static Long getUserId(){
        return (Long)initialThreadLocal().get();
    }

    public static void setUserId(Long userId){
        initialThreadLocal().set(userId);
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
