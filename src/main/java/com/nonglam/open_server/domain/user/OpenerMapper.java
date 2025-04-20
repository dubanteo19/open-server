package com.nonglam.open_server.domain.user;

import com.nonglam.open_server.domain.user.dto.request.OpenerUpdateRequest;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;
import org.springframework.stereotype.Component;

@Component
public class OpenerMapper {
    public void updateOpener(Opener currentOpener, OpenerUpdateRequest request) {
        currentOpener.setBio(request.bio());
        currentOpener.setDisplayName(request.displayName());
        currentOpener.setLocation(request.location());
    }

    public OpenerDetail toOpenerDetail(Opener opener) {
        var openerSummary = toOpenerResponse(opener);
        return new OpenerDetail(openerSummary,
                opener.getBio(),
                opener.getLocation(),
                opener.getJoinDate().toString(),
                opener.getFollowing().size(),
                opener.getFollowers().size()
        );
    }

    private OpenerResponse toOpenerResponse(Opener opener) {
        return new OpenerResponse(opener.getId(),
                opener.getUsername(),
                opener.getDisplayName(),
                opener.isVerified(),
                opener.getAvatarUrl()
        );
    }
}
