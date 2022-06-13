package com.example.todo.spec;

import com.example.todo.Enum.StatusEnum;
import com.example.todo.dto.PageParamRequest;
import com.example.todo.entity.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class TaskSpecification {

    public Specification<Task> getTaskSpecification(PageParamRequest pageParamRequest) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            String searchId = pageParamRequest.getId();
            if (StringUtils.isNotBlank(searchId)) {
                list.add(criteriaBuilder.equal(root.get("id").as(Long.class), searchId));
            }

            String searchSummary = pageParamRequest.getSummary();
            if (StringUtils.isNotBlank(searchSummary)) {
                list.add(criteriaBuilder.like(root.get("summary").as(String.class), "%" + searchSummary + "%"));
            }

            StatusEnum searchStatus = pageParamRequest.getStatus();
            if (!Objects.isNull(searchStatus)) {
                list.add(criteriaBuilder.equal(root.get("status").as(Long.class), searchStatus.getCode()));
            }

//            List<StatusEnum> statusList = pageParamRequest.getStatusList();
//            if (!Objects.isNull(searchStatus)) {
//                list.add(criteriaBuilder.and(root.get("status").as(Long.class).in(statusList)));
//            }

            LocalDate searchDueDate = pageParamRequest.getDueDate();
            if (!Objects.isNull(searchDueDate)) {
                list.add(criteriaBuilder.equal(root.get("dueDate").as(LocalDate.class), searchDueDate));
            }

            LocalDate startDueDate = pageParamRequest.getStartDate();
            if (!Objects.isNull(searchDueDate)) {
                list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate").as(LocalDate.class), startDueDate));
            }

            LocalDate endDueDate = pageParamRequest.getEndDate();
            if (!Objects.isNull(endDueDate)) {
                list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate").as(LocalDate.class), endDueDate));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
    }
}
