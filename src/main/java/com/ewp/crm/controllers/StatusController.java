package com.ewp.crm.controllers;

import com.ewp.crm.models.Notification;
import com.ewp.crm.models.Role;
import com.ewp.crm.models.Status;
import com.ewp.crm.models.User;
import com.ewp.crm.models.dto.StatusDtoForBoard;
import com.ewp.crm.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ewp.crm.util.Constants.*;
import static com.ewp.crm.util.Constants.ROLE_NAME_OWNER;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'OWNER', 'HR', 'MENTOR')")
@RequestMapping(value = "/status")
public class StatusController {
    private final StatusService statusService;
    private final MessageTemplateService messageTemplateService;

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    SlackService slackService;
    @Autowired
    NotificationService notificationService;

    @Autowired
    public StatusController(
            StatusService statusService,
            MessageTemplateService messageTemplateService) {
        this.statusService = statusService;
        this.messageTemplateService = messageTemplateService;
    }

    @GetMapping(value = "/{id}")
    public String showStatusClientsSorted(
            Model model,
            @PathVariable("id") String id,
            @AuthenticationPrincipal User userFromSession) {

        Long statusId = Long.parseLong(id);
        Optional<Status> optional = statusService.get(statusId);
        if (!(optional.isPresent())) {
            return "";
        }

        StatusDtoForBoard status = StatusDtoForBoard.getStatusDto(optional.get());
        Optional<Status> statusOptional = statusService.findStatusWithSortedClientsByUser(statusId, userFromSession);
        if (statusOptional.isPresent()) {
            status = StatusDtoForBoard.getStatusDto(statusOptional.get());
        }
        model.addAttribute("user", userFromSession);

        List<Role> sessionRoles = userFromSession.getRole();
        Role role = roleService.getRoleByName(ROLE_NAME_USER);
        if (sessionRoles.contains(roleService.getRoleByName(ROLE_NAME_MENTOR))) {
            role = roleService.getRoleByName(ROLE_NAME_MENTOR);
        }
        if (sessionRoles.contains(roleService.getRoleByName(ROLE_NAME_HR))) {
            role = roleService.getRoleByName(ROLE_NAME_HR);
        }
        if (sessionRoles.contains(roleService.getRoleByName(ROLE_NAME_ADMIN))) {
            role = roleService.getRoleByName(ROLE_NAME_ADMIN);
        }
        if (sessionRoles.contains(roleService.getRoleByName(ROLE_NAME_OWNER))) {
            role = roleService.getRoleByName(ROLE_NAME_OWNER);
        }

        List<User> userList = userService.getAll();
        List<Role> roles = roleService.getAll();
        List<StatusDtoForBoard> statuses = statusService.getStatusesForBoardByUserAndRole(userFromSession, role);
//        roles.remove(roleService.getRoleByName("OWNER"));
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", statuses);

        model.addAttribute("counter", new AtomicInteger(0));

        model.addAttribute("status", status);
        model.addAttribute("emailTmpl", messageTemplateService.getAll());
        model.addAttribute("users", userList.stream().filter(User::isVerified).collect(Collectors.toList()));
        model.addAttribute("users_without_mentors", userList.stream().filter(x -> !x.getRole().contains(roleService.getRoleByName("MENTOR"))).collect(Collectors.toList()));
        model.addAttribute("newUsers", userList.stream().filter(x -> !x.isVerified()).collect(Collectors.toList()));
        model.addAttribute("mentors", userList.stream().filter(x -> x.getRole().contains(roleService.getRoleByName("MENTOR"))).collect(Collectors.toList()));

        model.addAttribute("emailTmpl", messageTemplateService.getAll());

        model.addAttribute("slackWorkspaceUrl", slackService.getSlackWorkspaceUrl());

        model.addAttribute("notifications", notificationService.getByUserToNotify(userFromSession));
        model.addAttribute("notifications_type_sms", notificationService.getByUserToNotifyAndType(userFromSession, Notification.Type.SMS));
        model.addAttribute("notifications_type_comment", notificationService.getByUserToNotifyAndType(userFromSession, Notification.Type.COMMENT));
        model.addAttribute("notifications_type_postpone", notificationService.getByUserToNotifyAndType(userFromSession, Notification.Type.POSTPONE));
        model.addAttribute("notifications_type_new_user", notificationService.getByUserToNotifyAndType(userFromSession, Notification.Type.NEW_USER));


        return "fragments/htmlFragments::clientsForStatus";
    }

}
