package com.example.primaryproblem.exception.handler;

import com.example.primaryproblem.Utils.HttpUtils;
import com.example.primaryproblem.Utils.R;
import com.example.primaryproblem.constant.ExceptionMessageEnum;
import com.example.primaryproblem.constant.HandleStatus;
import com.example.primaryproblem.exception.RepeatedGeneratePaperExpection;
import com.example.primaryproblem.exception.RepeatedSubmitAnswerExpection;
import com.example.primaryproblem.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@ControllerAdvice
public class ProblemInfoExceptionHandler {

    @Autowired
    private TokenService tokenService;

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R problemInfoExceptionHandler(MethodArgumentNotValidException exception) {

        System.out.println("problemInfoExceptionHandler.....");
        BindingResult bindingResult = exception.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Iterator<FieldError> iterator = fieldErrors.iterator();

        HashMap<String, String> hashMap = new HashMap<>(fieldErrors.size());

        while (iterator.hasNext()) {
            FieldError error = iterator.next();
            hashMap.put(error.getField(), error.getDefaultMessage());
        }

        final String markHeader = HttpUtils.getHttpResponse().getHeader(HandleStatus.STATUS_MARK.getHandleKey());

        if (markHeader == null || markHeader.equals(HandleStatus.DELTOKEN_ADN_HANDLE.getStatus())) {
            return R.error(ExceptionMessageEnum.METHOD_ARGUMENTS_NOT_VALIED.getCode(), ExceptionMessageEnum.METHOD_ARGUMENTS_NOT_VALIED.getMessage()).put("errorMap", hashMap);
        } else {
            final String atomToken = tokenService.generateAtomToken();

            return R.error(ExceptionMessageEnum.METHOD_ARGUMENTS_NOT_VALIED.getCode(), ExceptionMessageEnum.METHOD_ARGUMENTS_NOT_VALIED.getMessage()).put("errorMap", hashMap)
                    .put("atomToken", atomToken);

        }

    }

    @ResponseBody
    @ExceptionHandler(value = RepeatedGeneratePaperExpection.class)
    public R RepeatedGeneratePaperExpectionExpectionHandler(RepeatedGeneratePaperExpection exception) {

        return R.error(ExceptionMessageEnum.REPEATED_GENERATE_PAPER.getCode(), ExceptionMessageEnum.REPEATED_GENERATE_PAPER.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = RepeatedSubmitAnswerExpection.class)
    public R RepeatedSubmitAnswerExpectionHandler(RepeatedSubmitAnswerExpection exception) {

        return R.error(ExceptionMessageEnum.PRPEATED_SUBMIT_ANSWER.getCode(), ExceptionMessageEnum.PRPEATED_SUBMIT_ANSWER.getMessage());
    }

    // 处理通用未知异常
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R commonExceptionHandler(Exception e) {

        return R.error(ExceptionMessageEnum.UNKNOWN_EXCEPTION.getCode(), ExceptionMessageEnum.UNKNOWN_EXCEPTION.getMessage());
    }


}
