//package com.dhk.kernel.exception;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.beetl.sql.core.BeetlSQLException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindException;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.xiaxiang.fantasyar.material.handlertool.define.ErrorCodeMsg;
//import com.xiaxiang.fantasyar.material.handlertool.define.HttpStatuDefine;
//import com.xiaxiang.fantasyar.material.handlertool.result.ErrorResult;
//import com.xiaxiang.fantasyar.material.handlertool.result.GlobalResult;
//
//
//
///**
// * 
//* @ClassName: GlobalExceptionHandler 
//* @Description: TODO
//* @author ZZL
//* @date 2017年10月9日 下午3:05:42 
//*
// */
//@ControllerAdvice 
//@ResponseBody
//public class GlobalExceptionHandler {
//
//	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//	/**
//	 * .非法参数异常
//	 */
//	@ExceptionHandler(ParamInvalidException.class)
//	public GlobalResult handleParamInvalidException(HttpServletRequest request, HttpServletResponse response,
//			ParamInvalidException e, Model model) {
//		logger.error("path: {} errorCode: {} errorMsg: {}", e.getClass().getName(), e.getCode(), e.getMsg(), e);
//		ErrorResult errorResult = new ErrorResult(e.getCode(), e.getMsg());
//		return returnGlobalResult(request, errorResult);
//	}
//
//	/**
//	 * .权限异常
//	 */
//	@ExceptionHandler(PermissionDeniedException.class)
//	public GlobalResult handlePermissionDeniedException(HttpServletRequest request, HttpServletResponse response, PermissionDeniedException e,Model model) {
//		logger.error("path: {} errorCode: {} errorMsg: {}", e.getClass().getSimpleName(), e.getCode(), e.getMsg(), e);
//		ErrorResult errorResult = new ErrorResult(e.getCode(), e.getMsg());
//		return returnGlobalResult(request, errorResult);
//	}
//
//	/**
//	 * .未知异常
//	 */
//	@ExceptionHandler(UnknownErrorException.class)
//	public GlobalResult handleUnknownErrorException(HttpServletRequest request, HttpServletResponse response, UnknownErrorException e,Model model) {
//		logger.error("path: {} errorCode: {} errorMsg: {}", e.getClass().getSimpleName(), e.getCode(), e.getMsg(), e);
//		ErrorResult errorResult = new ErrorResult(e.getCode(), e.getMsg());
//		return returnGlobalResult(request, errorResult);
//	}
//
//	/**
//	 * .网络异常
//	 */
//	@ExceptionHandler(NetworkErrorException.class)
//	public GlobalResult handleNetworkErrorException(HttpServletRequest request, HttpServletResponse response, NetworkErrorException e,Model model) {
//		logger.error("path: {} errorCode: {} errorMsg: {}", e.getClass().getSimpleName(), e.getCode(), e.getMsg(), e);
//		ErrorResult errorResult = new ErrorResult(e.getCode(), e.getMsg());
//		return returnGlobalResult(request, errorResult);
//	}
//
//	/**
//	 * .系统内部错误异常
//	 */
//	@ExceptionHandler(InnerErrorException.class)
//	public GlobalResult handleInnerErrorException(HttpServletRequest request, HttpServletResponse response, InnerErrorException e,Model model) {
//		logger.error("path: {} errorCode: {} errorMsg: {}", e.getClass().getSimpleName(), e.getCode(), e.getMsg(), e);
//		ErrorResult errorResult = new ErrorResult(e.getCode(), e.getMsg());
//		return returnGlobalResult(request, errorResult);
//	}
//
//	/**
//	 * .空指针异常
//	 */
//	@ExceptionHandler(NullPointerException.class)
//	public GlobalResult handleNullPointerException(HttpServletRequest request,HttpServletResponse response, NullPointerException e,Model model) {
//		logger.error("path: {} ", e.getClass().getSimpleName(), e);
//		InnerErrorException innerErrorException = new InnerErrorException();
//		ErrorResult errorResult = new ErrorResult(innerErrorException.getCode(), innerErrorException.getMsg());
//		return returnGlobalResult(request, errorResult);
//	}
//
//	/**
//	 * .空指针异常
//	 */
//	@ExceptionHandler(BindException.class)
//	public GlobalResult handleBindException(HttpServletRequest request, HttpServletResponse response, BindException e,Model model) {
//		logger.error("path: {} ", e.getClass().getSimpleName(), e);
//		BindingResult bindingResult = e.getBindingResult();
//		ParamInvalidException paramInvalidException = new ParamInvalidException();
//		ErrorResult errorResult = new ErrorResult(paramInvalidException.getCode(), bindingResult.getFieldError().getDefaultMessage());
//		return returnGlobalResult(request, errorResult);
//	}
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public GlobalResult handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e,Model model) {
//		logger.error("path: {} ", e.getClass().getSimpleName(), e);
//		BindingResult bindingResult = e.getBindingResult();
//		ParamInvalidException paramInvalidException = new ParamInvalidException();
//		ErrorResult errorResult = new ErrorResult(paramInvalidException.getCode(), bindingResult.getFieldError().getDefaultMessage());
//		return returnGlobalResult(request, errorResult);
//	}
//	/**
//	 * .程序中未能考虑到的异常
//	 */
//	@ExceptionHandler(Exception.class)
//	public GlobalResult handleException(HttpServletRequest request, HttpServletResponse response,  Exception e,Model model) {
//		logger.error("path: {} ", e.getClass().getSimpleName(), e);
//		ErrorResult errorResult = new ErrorResult(HttpStatuDefine.FAIL, e.getMessage());
//		return returnGlobalResult(request, errorResult);
//	}
//
//	/**
//	 * 
//		* @Title: MissingServletRequestParameterException 
//		* @Description: 数据绑定异常
//		* @param @param request
//		* @param @param response
//		* @param @param e
//		* @param @param model
//		* @param @return    设定文件 
//		* @return GlobalResult    返回类型 
//		* @throws
//	 */
//	@ExceptionHandler(MissingServletRequestParameterException.class)
//	public GlobalResult handleMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response, MissingServletRequestParameterException e,Model model) {
//		logger.error("path: {} ", e.getClass().getSimpleName(), e);
//		ParamInvalidException paramInvalidException = new ParamInvalidException(ErrorCodeMsg.PARAM_INVALID);
//		ErrorResult errorResult = new ErrorResult(paramInvalidException.getCode(), e.getParameterName() == null ? "" : e.getParameterName() + paramInvalidException.getMsg());
//	    return returnGlobalResult(request, errorResult);
//	}
//	
//	/**
//	 * .sql异常
//	 */
//	@ExceptionHandler(BeetlSQLException.class)
//	public GlobalResult handleBeetlSQLException(HttpServletRequest request, HttpServletResponse response, BeetlSQLException e,Model model) {
//		logger.error("path: {} ", e.getClass().getSimpleName(), e);
//		ErrorResult errorResult = new ErrorResult(HttpStatuDefine.FAIL, e.getMessage());
//	    return returnGlobalResult(request, errorResult);
//	}
//	
//	/**
//	 * 
//	* @Title: isAjax 
//	* @Description: 判断是否是ajax请求
//	* @param @param request
//	* @param @return    设定文件 
//	* @return boolean    返回类型 
//	* @throws
//	 */
///*	private boolean isAjax(HttpServletRequest request){
//		if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){ 
//            //如果是ajax请求响应头会有，x-requested-with  
//            System.out.print("发生ajax请求...");
//            return true;
//            //response.setHeader("sessionstatus", "timeout");//在响应头设置session状态  
//        }else{
//        	return false;
//        }
//	}*/
//	
//	/**
//	 * 
//	* @Title: returnFront 
//	* @Description: 返回异常数据统一封装
//	* @param @param request
//	* @param @param response
//	* @param @param model
//	* @param @param errorResult
//	* @param @return    设定文件 
//	* @return String    返回类型 
//	* @throws
//	 */
///*	private String returnFront(HttpServletRequest request, HttpServletResponse response,Model model,ErrorResult errorResult ){
//		boolean isajax = isAjax(request);
//		if (isajax) {
//			ResponseUtils.render(response, JSON.toJSONString(errorResult));
//			return null;
//		} else {
//			model.addAttribute("globalResult",
//					errorResult);
//			return "error";
//		}
//	}*/
//	
//	public GlobalResult returnGlobalResult(HttpServletRequest request,ErrorResult errorResult){
//		return new GlobalResult(HttpStatuDefine.FAIL, request.getRequestURI(), errorResult);
//	}
//}
