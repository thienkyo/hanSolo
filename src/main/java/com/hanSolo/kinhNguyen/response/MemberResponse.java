package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.Member;

public class MemberResponse extends BaseResponse{
    private Member member;

    public MemberResponse(Member member, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
