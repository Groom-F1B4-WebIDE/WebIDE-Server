package f1b4.webide_server.service;

import f1b4.webide_server.dto.MemberDTO;
import f1b4.webide_server.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import f1b4.webide_server.repository.MemberRepository;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
