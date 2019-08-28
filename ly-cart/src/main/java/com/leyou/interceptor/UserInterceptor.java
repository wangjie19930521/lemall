package com.leyou.interceptor;

import com.leyou.config.JwtProperties;
import com.leyoumall.common.utils.CookieUtils;
import com.leyoumall.entity.UserInfo;
import com.leyoumall.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserIntercepyor
 * @Description:   要让拦截器生效   需要写配置类
 * @Author wangJ1e
 * @Date 2019-08-20
 * @Version V1.0
 **/
@Slf4j

public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> t1 = new ThreadLocal();   // todo 创建线程域

   public UserInterceptor(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
   }


    /**
     * @MethodName: preHandle
     * @Description: 在controller之前执行
     * @Param: [request, response, handler]
     * @Return: boolean
     * @Author: wangJ1e
     * @Date: 2019-08-20
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取cookie 中的token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        //解析token
        try {
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //设置同一个request中的user   使其他controller能获取到user    跨服务之间  不是同一个 toncat  所以不能setAttribute
            //同一个请求，线程也是共享的，也可以通过线程传递  TODO    拦截器通过之后传递用户信息给之后的controller
            //存到线程域，map结构，key为线程本生，保证不同线程不共享，
            /*request.setAttribute("user",infoFromToken);*/   //方式一
            //通过线程域  //方式儿
            t1.set(infoFromToken);  /*只有value  因为key为线程本身*/
            //放行
            return true;
        }catch (Exception e){
            log.error("用户拦截器解析token失败！",e);
        }
        return false;

    }

    /**
     * @MethodName: afterCompletion
     * @Description: 在视图渲染完成后执行
     * @Param: [request, response, handler, ex]
     * @Return: void
     * @Author: wangJ1e
     * @Date: 2019-08-20
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //通过线程域的方式，完城之后需要删除 携带的信息
        t1.remove();
    }

    /**
     * @MethodName: postHandle
     * @Description: 在controller之后执行
     * @Param: [request, response, handler, modelAndView]
     * @Return: void
     * @Author: wangJ1e
     * @Date: 2019-08-20
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }
    //提供获取user的方法 todo   this.  不能再static里面   static属于类，不属于对象 this代表当前对象
    public static UserInfo getLoginUser(){
        return t1.get();
    }
}
