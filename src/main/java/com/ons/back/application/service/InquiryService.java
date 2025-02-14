package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Inquiry;
import com.ons.back.persistence.repository.InquiryRepository;
import com.ons.back.presentation.dto.request.AnswerInquiryRequest;
import com.ons.back.presentation.dto.request.CreateInquiryRequest;
import com.ons.back.presentation.dto.response.ReadUnAnsweredInquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public List<ReadUnAnsweredInquiryResponse> getInquiryUnAnswered() {
        return inquiryRepository.findByIsAnsweredFalse().stream().map(ReadUnAnsweredInquiryResponse::fromEntity).toList();
    }

    public void answerInquiry(AnswerInquiryRequest request) {

        Inquiry inquiry = inquiryRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 질문이 없습니다.", 404, LocalDateTime.now())
                ));

        inquiry.updateAnswer(request.answer());
    }

    public void createInquiry(CreateInquiryRequest request) {
        inquiryRepository.save(request.toEntity());
    }
}
