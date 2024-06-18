package f1b4.webide_server.member.service;

import f1b4.webide_server.member.dto.MemberDTO;
import f1b4.webide_server.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import f1b4.webide_server.member.repository.MemberRepository;

import java.util.Optional;
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberDTO signup(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        return MemberDTO.toMemberDTO(memberEntity);
    }
    public boolean existsByEmail(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberEmailAndMemberPassword(
                memberDTO.getMemberEmail(), memberDTO.getMemberPassword());
        if (memberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(memberEntity.get());
        } else {
            return null;
        }
    }
}
