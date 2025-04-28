package com.mindproapps.jira.integracaoponto.model.dto.ponto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Log4j
public class PontoDTO {
    @SerializedName("Matricula")
    private Integer matricula;
    @SerializedName("Email")
    private String email;
    @SerializedName("DataInformacao")
    private String dataInformacao;
    @SerializedName("QtdeHorasTrabalhadas")
    private String qtdeHorasTrabalhadas;
    @SerializedName("QtdeHorasExtras")
    private String qtdeHorasExtras;
    private final static long serialVersionUID = -2352597681810731292L;

    public Double getHorasTrabalhadas() {
        if(this.qtdeHorasTrabalhadas != null &&
                this.qtdeHorasTrabalhadas.indexOf(":") != -1 &&
                this.qtdeHorasTrabalhadas.lastIndexOf(":") != this.qtdeHorasTrabalhadas.indexOf(":")) {
            try {
                String[] values = this.qtdeHorasTrabalhadas.split(":");
                return (Double.valueOf(values[0]) + (Double.valueOf(values[1]) / 60) + Double.valueOf(values[2]) / 3600);
            } catch (Exception e) {
                log.error("PontoDTO: invalid value for qtdeHorasTrabalhadas: " + this.qtdeHorasTrabalhadas);
            }
        }
        return 0.0;
    }

    public Double getHorasExtras() {
        if(this.qtdeHorasExtras != null &&
                this.qtdeHorasExtras.indexOf(":") != -1 &&
                this.qtdeHorasExtras.lastIndexOf(":") != this.qtdeHorasExtras.indexOf(":")) {
            try {
                String[] values = this.qtdeHorasExtras.split(":");
                return (Double.valueOf(values[0]) + (Double.valueOf(values[1]) / 60) + Double.valueOf(values[2]) / 3600);
            } catch (Exception e) {
                log.error("PontoDTO: invalid value for qtdeHorasExtras: " + this.qtdeHorasExtras);
            }
        }
        return 0.0;
    }

    public String getLogin() {
        String[] values = this.email.split("@");
        return values[0];
    }
}
