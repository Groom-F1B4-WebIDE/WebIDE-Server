package f1b4.webide_server.member.service;

import f1b4.webide_server.member.dto.MemberDTO;
import f1b4.webide_server.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import f1b4.webide_server.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);

    }
    public MemberDTO login(MemberDTO memberDTO){
        Optional<MemberEntity> SubmitMail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(SubmitMail.isPresent()){
            MemberEntity memberEntity = SubmitMail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto ;
            }
            else{
                return null;
            }
        }
        else {
            return null;
        }
    }

}
