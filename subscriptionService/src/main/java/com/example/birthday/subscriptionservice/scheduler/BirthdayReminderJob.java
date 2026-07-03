package com.example.birthday.subscriptionservice.scheduler;

import com.example.birthday.subscriptionservice.client.UserClient;
import com.example.birthday.subscriptionservice.model.*;
import com.example.birthday.subscriptionservice.repository.*;
import com.example.birthday.subscriptionservice.service.OutboxService;
import com.example.birthday.subscriptionservice.util.BirthdayUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Map;
import java.util.UUID;

@Component
public class BirthdayReminderJob {
    private final SubscriptionRepository subscriptions;
    private final ReminderDispatchRepository dispatches;
    private final UserClient users;
    private final OutboxService outbox;
    private final Clock clock;

    public BirthdayReminderJob(
            SubscriptionRepository subscriptions,
            ReminderDispatchRepository dispatches,
            UserClient users,
            OutboxService outbox,
            Clock clock) {
        this.subscriptions = subscriptions;
        this.dispatches = dispatches;
        this.users = users;
        this.outbox = outbox;
        this.clock = clock;
    }

    @Scheduled(cron = "${reminders.cron:0 0 9 * * *}", zone = "${reminders.zone:UTC}")
    @Transactional
    public void run() {
        LocalDate today = LocalDate.now(clock);
        for (var subscription : subscriptions.findAllByTypeAndActiveTrue(
                SubscriptionType.USER)) {
            var target = users.get(subscription.getTargetUserId());
            LocalDate birthday = BirthdayUtils.nextBirthday(target.birthDate(), today);
            if (BirthdayUtils.daysUntil(target.birthDate(), today) != subscription.getDaysBefore()
                    || dispatches.existsBySubscriptionIdAndBirthdayYear(
                    subscription.getId(), birthday.getYear())) {
                continue;
            }
            var dispatch = new ReminderDispatch();
            dispatch.setSubscriptionId(subscription.getId());
            dispatch.setBirthdayYear(birthday.getYear());
            dispatches.save(dispatch);
            outbox.append(
                    "birthday.reminder.requested",
                    "BirthdayReminderRequestedEvent",
                    subscription.getId(),
                    "birthday-job-" + UUID.randomUUID(),
                    Map.of(
                            "subscriptionId", subscription.getId().toString(),
                            "subscriberId", subscription.getSubscriberId().toString(),
                            "targetUserId", target.id().toString(),
                            "targetName", target.firstName(),
                            "birthday", birthday.toString(),
                            "daysBefore", subscription.getDaysBefore()));
        }
    }
}
