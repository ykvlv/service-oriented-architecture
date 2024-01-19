package soa.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import soa.ejb.model.TicketDto;
import soa.ejb.model.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Generated;
import javax.validation.constraints.Min;

@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-09-22T15:03:58.143327300+03:00[Europe/Moscow]")
@Validated
public interface OrgdirectoryApi {

    @Operation(summary = "Создать VIP билет", description = "Создает VIP билет от предыдущего билета", tags={ "filtration" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает созданный билет", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketDto.class))),

            @ApiResponse(responseCode = "400", description = "Запрос не удался", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @RequestMapping(value = "/booking/sell/vip/{ticket-id}/{person-id}",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<?> increaseStepsCount(
            @Min(0L) @Parameter(in = ParameterIn.PATH, description = "ИД Билета",
                    required=true, schema=@Schema())
            @PathVariable("ticket-id") Integer ticketId,
            @Min(0L) @Parameter(in = ParameterIn.PATH, description = "ИД человека",
                    required=true, schema=@Schema())
            @PathVariable("person-id") Integer personId);

    @Operation(summary = "Создать билет со скидкой", description = "Создать билет со скидкой че то там рассчитав", tags={ "filtration" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает созданный билет", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketDto.class))),

            @ApiResponse(responseCode = "400", description = "Запрос не удался", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @RequestMapping(value = "/booking/sell/discount/{ticket-id}/{person-id}/{discount}",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<?> makeDiscount(
            @Min(0L) @Parameter(in = ParameterIn.PATH, description = "ИД Билета",
                    required=true, schema=@Schema())
            @PathVariable("ticket-id") Integer ticketId,
            @Min(0L) @Parameter(in = ParameterIn.PATH, description = "ИД Билета",
                    required=true, schema=@Schema())
            @PathVariable("person-id") Integer personId,
            @Min(0L) @Parameter(in = ParameterIn.PATH, description = "ИД человека",
                    required=true, schema=@Schema())
            @PathVariable("discount") Double discount);
}

