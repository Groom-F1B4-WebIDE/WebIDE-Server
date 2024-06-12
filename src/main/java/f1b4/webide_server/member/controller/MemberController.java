package f1b4.webide_server.member.controller;

import f1b4.webide_server.member.dto.MemberDTO;
import f1b4.webide_server.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO)
    {
        memberService.save(memberDTO);
        return "login";
    }

        @PostMapping("/member/login")
        public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            // login 실패
            return "login";
         }
        }
    }
