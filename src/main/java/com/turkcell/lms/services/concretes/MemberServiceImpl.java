package com.turkcell.lms.services.concretes;

import com.turkcell.lms.entities.Member;
import com.turkcell.lms.repositories.MemberRepository;
import com.turkcell.lms.services.abstracts.MemberService;
import com.turkcell.lms.services.dtos.requests.member.AddMemberRequest;
import com.turkcell.lms.services.dtos.requests.member.UpdateMemberRequest;
import com.turkcell.lms.services.dtos.responses.member.AddMemberResponse;
import com.turkcell.lms.services.dtos.responses.member.GetByIdMemberResponse;
import com.turkcell.lms.services.dtos.responses.member.ListMemberResponse;
import com.turkcell.lms.services.dtos.responses.member.UpdateMemberResponse;
import com.turkcell.lms.services.mappers.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private final MemberRepository memberRepository;

//  WITH AUTO MAPPING
    public List<ListMemberResponse> getAll(){

        List<Member> members = memberRepository.findAll();

        return MemberMapper.INSTANCE.membersToListMemberResponses(members);
    }

    public Optional<GetByIdMemberResponse> getById(int id) {
        Optional<Member> memberOptional = memberRepository.findById(id);

        return memberOptional.map(MemberMapper.INSTANCE::mapToGetByIdMemberResponse);
    }

    public void deleteMemberById(int id){
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("Member with ID " + id + " does not exist");
        }
        memberRepository.deleteById(id);
    }

    @Override
    public AddMemberResponse addMember(AddMemberRequest request) {

        if(request.getName().length() < 3)
            throw new RuntimeException("Member name should be at least 3 letters long.");
        // Auto Mapping utilizing MapStruck
        Member member = MemberMapper.INSTANCE.memberFromRequest(request);
        Member savedMember = memberRepository.save(member);
        AddMemberResponse response = new AddMemberResponse(savedMember.getId(),
                savedMember.getName(),
                savedMember.getSurname(),
                savedMember.getPassword(),
                savedMember.getEmail());

        return response;
    }

    public UpdateMemberResponse updateMember(int id, UpdateMemberRequest request) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        UpdateMemberResponse response = new UpdateMemberResponse();

        memberOptional.ifPresent(member -> {
            Member updatedMember = MemberMapper.INSTANCE.updateMemberFromRequest(id, member);
            Member savedMember = memberRepository.save(updatedMember);

            response.setId(savedMember.getId());
            response.setName(savedMember.getName());
            response.setSurname(savedMember.getSurname());
            response.setPassword(savedMember.getPassword());
            response.setEmail(savedMember.getEmail());
        });

        return response;
    }

}

//  WITH MANUAL MAPPING

//   public Optional<GetByIdMemberResponse> getById(int id) {
//    Optional<Member> memberOptional = memberRepository.findById(id);
//
//    if (memberOptional.isPresent()) {
//        Member member = memberOptional.get();
//
//        GetByIdMemberResponse response = new GetByIdMemberResponse();
//        response.setId(member.getId());
//        response.setName(member.getName());
//        response.setSurname(member.getSurname());
//        response.setEmail(member.getEmail());
//        response.setPassword(member.getPassword());
//
//        return Optional.of(response);
//    } else {
//        return Optional.empty();
//    }
//}

//Alternatively

//public Optional<GetByIdMemberResponse> getById(int id) {
//    Optional<Member> memberOptional = memberRepository.findById(id);
//
//    return memberOptional.map(member -> {
//        GetByIdMemberResponse response = new GetByIdMemberResponse();
//        response.setId(member.getId());
//        response.setName(member.getName());
//        response.setSurname(member.getSurname());
//        response.setEmail(member.getEmail());
//        response.setPassword(member.getPassword());
//        return response;
//    });
//}


//public AddMemberResponse addMember(AddMemberRequest request) {
//
//    if(request.getName().length() < 3)
//        throw new RuntimeException("Member ismi en az 3 hane olmalıdır.");
//    Member member = new Member();
//    member.setName(request.getName());
//    member.setSurname(request.getSurname());
//    member.setEmail(request.getEmail());
//    member.setPassword(request.getPassword());
//    member.setMemberNumber(request.getMemberNumber());
//    Member savedMember= memberRepository.save(member);
//    AddMemberResponse response= new AddMemberResponse(savedMember.getId(),
//            savedMember.getName(),
//            savedMember.getSurname(),
//            savedMember.getEmail(),
//            savedMember.getPassword()
//    );
//
//    return response;
//}


//public UpdateMemberResponse updateMember(int id, UpdateMemberRequest request) {
//    Optional<Member> memberOptional = memberRepository.findById(id);
//    UpdateMemberResponse response = new UpdateMemberResponse();
//
//    memberOptional.ifPresent(member -> {
//        member.setEmail(request.getEmail());
//        member.setPassword(request.getPassword());
//        memberRepository.save(member);
//
//        response.setId(member.getId());
//        response.setName(member.getName());
//        response.setSurname(member.getSurname());
//        response.setPassword(member.getPassword());
//        response.setEmail(member.getEmail());
//    });
//
//    return response;
//}

