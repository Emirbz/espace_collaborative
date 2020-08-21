package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Badge;
import io.accretio.Models.User;
import io.accretio.Repository.BadgeRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class BadgeService {

    @Inject
    BadgeRepository badgeRepository;

    String lockedSmall = "badges/locked_s.png";
    String lockedBig = "badges/locked_b.png";


    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);


    public List<Badge> getAllBadges() {
        return badgeRepository.listAll();
    }


    public List<Badge> getMyBadges(User user) {
        List<Badge> badgeList = getAllBadges();
        badgeList.stream().filter(badge -> user.getBadge().getId() < badge.getId()).forEach(badge -> {
            badge.setIcon_b(lockedBig);
            badge.setIcon_s(lockedSmall);
        });
        return badgeList;
    }
}
