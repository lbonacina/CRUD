package CRUD.controller;

import CRUD.model.Member;
import CRUD.service.MemberService;
import CRUD.util.BeanLifecycleTracker;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.Conversation;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ConversationScoped;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named("two_member_crud")
@ConversationScoped
public class TwoPageMemberCRUD implements Serializable {

// ------------------------------ FIELDS ------------------------------

    private static final long serialVersionUID = 2729352860728646778L;

    @Inject private Logger log;

    @Inject Conversation conversation;

    @Inject private MemberService memberService;

    private List<Member> members;

    private Member selectedMember;

// -------------------------- OTHER METHODS --------------------------

    public String back() {
        conversation.close();
        return "index?faces-redirect=true";
    }

    public String create() {
        log.info("creating new member");
        selectedMember = new Member();
        return "edit";
    }

    public void delete(Member member) {
        log.info("deleting member : " + member.toString());
        memberService.delete(member);
        selectedMember = new Member();
    }

    public String edit(Member member) {
        log.info("editing member : " + member.toString());
        selectedMember = member;
        return "edit?faces-redirect=true";
    }

    @PostConstruct
    public void init() {
        log.info("===== TwoPageMemberCRUD::PostConstruct =====");
        selectedMember = new Member();
        members = memberService.findAllOrderedByName();
    }

    @PreDestroy
    public void destroy() {
        log.info("===== TwoPageMemberCRUD::PreDestroy =====");
    }

    public void save() throws Exception {

        log.info("saving member : " + selectedMember.toString());
        try {
            memberService.save(selectedMember);
            Messages.addGlobalInfo("Saving Successful");
        }
        catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            Messages.addGlobalError("Saving unsuccessful");
        }
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

    public void updateAfterSave(@Observes(notifyObserver= Reception.IF_EXISTS) Member member) {
        members = memberService.findAllOrderedByName();
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public List<Member> getMembers() {
        return members;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }
}
