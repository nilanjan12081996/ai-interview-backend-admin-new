package resume.miles.questionAwarenessmap.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import resume.miles.questionAwarenessmap.dto.AwarenessQuestionMapDTO;
import resume.miles.questionAwarenessmap.entity.AwarenessQuestionMap;
import resume.miles.questions.mapper.QuestionAnswerMappper;

public class AwarnessQuestionMapper {
    public static AwarenessQuestionMapDTO toDtoMapper(AwarenessQuestionMap awarenessQuestionMap){
            AwarenessQuestionMapDTO awarenessQuestionMapdata = AwarenessQuestionMapDTO.builder()
                                                        .id(awarenessQuestionMap.getId())
                                                        .awarenessId(awarenessQuestionMap.getAwarenessId())
                                                        .questionId(awarenessQuestionMap.getQuestionId())
                                                        .status(awarenessQuestionMap.getStatus())
                                                        .createdAt(awarenessQuestionMap.getCreatedAt())
                                                        .updatedAt(awarenessQuestionMap.getUpdatedAt())
                                                        .build();

            return awarenessQuestionMapdata;
    }
     public static AwarenessQuestionMapDTO toDtoMapperForRelation(AwarenessQuestionMap awarenessQuestionMap){
            AwarenessQuestionMapDTO awarenessQuestionMapdata = AwarenessQuestionMapDTO.builder()
                                                        .id(awarenessQuestionMap.getId())
                                                        .awarenessId(awarenessQuestionMap.getAwarenessId())
                                                        .questionId(awarenessQuestionMap.getQuestionId())
                                                        .question(QuestionAnswerMappper.toQuestionDTO(awarenessQuestionMap.getQuestion()))
                                                        .status(awarenessQuestionMap.getStatus())
                                                        .createdAt(awarenessQuestionMap.getCreatedAt())
                                                        .updatedAt(awarenessQuestionMap.getUpdatedAt())
                                                        .build();

            return awarenessQuestionMapdata;
    }
    public static AwarenessQuestionMap toEntityMapper(AwarenessQuestionMapDTO awarenessQuestionMap){
            AwarenessQuestionMap awarenessQuestionMapdata = AwarenessQuestionMap.builder()
                                                        .id(awarenessQuestionMap.getId())
                                                        .awarenessId(awarenessQuestionMap.getAwarenessId())
                                                        .questionId(awarenessQuestionMap.getQuestionId())
                                                        .status(awarenessQuestionMap.getStatus())
                                                        .createdAt(awarenessQuestionMap.getCreatedAt())
                                                        .updatedAt(awarenessQuestionMap.getUpdatedAt())
                                                        .build();

            return awarenessQuestionMapdata;
    }

     public static List<AwarenessQuestionMapDTO> toDtoList(List<AwarenessQuestionMap> awarenessQuestionMap){
            List<AwarenessQuestionMapDTO> awarenessQuestionMapdata = awarenessQuestionMap.stream().map(data->toDtoMapperForRelation(data)).collect(Collectors.toList());

            return awarenessQuestionMapdata;
    }
}
