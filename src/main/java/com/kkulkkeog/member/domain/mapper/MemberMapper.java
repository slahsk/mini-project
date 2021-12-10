package com.kkulkkeog.member.domain.mapper;

import com.kkulkkeog.member.api.web.*;
import com.kkulkkeog.member.domain.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Member toMember(PostMemberRequest request);

    Member toMember(Long no, PutMemberRequest request);

    MemberResponse toMemberResponse(Member member);
}
