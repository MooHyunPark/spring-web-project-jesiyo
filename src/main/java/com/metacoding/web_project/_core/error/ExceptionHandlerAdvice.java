package com.metacoding.web_project._core.error;

import com.metacoding.web_project._core.error.ex.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ResponseBody
    @ExceptionHandler(Exception400.class)
    public String err400(Exception400 e) {
        String message = e.getMessage().replace("'", "\\'");
        String body = """
                <script>
                    alert('${msg}');
                    history.back();
                </script>
                """.replace("${msg}", message);

        return body;
    }

    @ResponseBody
    @ExceptionHandler(Exception401.class)
    public String err401(Exception401 e) {
        String message = e.getMessage().replace("'", "\\'");
        String body = """
                <script>
                    alert('${msg}');
                    history.back();
                </script>
                """.replace("${msg}", message);

        return body;
    }

    @ResponseBody
    @ExceptionHandler(Exception403.class)
    public String err403(Exception403 e) {
        String message = e.getMessage().replace("'", "\\'");
        String body = """
                <script>
                    alert('${msg}');
                    history.back();
                </script>
                """.replace("${msg}", message);

        return body;
    }


    @ResponseBody
    @ExceptionHandler(Exception404.class)
    public String err404(Exception404 e) {
        String message = e.getMessage().replace("'", "\\'");
        String body = """
                <script>
                    alert('${msg}');
                    history.back();
                </script>
                """.replace("${msg}", message);

        return body;
    }

    @ResponseBody
    @ExceptionHandler(Exception500.class)
    public String err500(Exception500 e) {
        String message = e.getMessage().replace("'", "\\'");
        String body = """
                <script>
                    alert('${msg}');
                    history.back();
                </script>
                """.replace("${msg}", message);

        return body;
    }

    @ExceptionHandler(Exception400Json.class)
    public ResponseEntity<String> Exception400Json(Exception400Json ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
