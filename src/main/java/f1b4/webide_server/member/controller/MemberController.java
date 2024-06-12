package f1b4.webide_server.member.controller;

import f1b4.webide_server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private  final MemberService memberService;

    @GetMapping("/member/save")
    public  String saveForm(){
        return "save";
    }
    @GetMapping("/member/login")
    public String loginForm(){
        return "login";
    }
}
