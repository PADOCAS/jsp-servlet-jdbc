/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 *
 * @author lucia
 */
public class ReportUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Metodo generico para gerar Relatorio PDF de acordo com os parametros
     * recebidos
     *
     * @param listDados Recebe uma listaDados generica
     * @param nomeRelatorio Recebe o nome do relatario
     * @param params Recebe Parametros para o relatorio
     * @param servletContext Recebe o servlet envolvido, para saber da onde
     *
     * @return Retorno o relatorio impresso em byte[]
     * @throws java.lang.Exception
     */
    public byte[] geraRelatorioPDF(List listDados, String nomeRelatorio, Map<String, Object> params, ServletContext servletContext) throws Exception {
        //Cria a lista de Dados que vem por parametro:
        JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(listDados);

        //Caminho ficara sempre na pasta relatorio:
        String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";

        //Caminho do Relatorio, dados e gera a impressao:
        JasperPrint jasperPrint = JasperFillManager.fillReport(caminhoJasper, params, jRBeanCollectionDataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    /**
     * Metodo generico para gerar Relatorio Excel de acordo com os parametros
     * recebidos
     *
     * @param listDados Recebe uma listaDados generica
     * @param nomeRelatorio Recebe o nome do relatario
     * @param params Recebe Parametros para o relatorio
     * @param servletContext Recebe o servlet envolvido, para saber da onde
     *
     * @return Retorno o relatorio impresso em byte[]
     * @throws java.lang.Exception
     */
    public byte[] geraRelatorioExcel(List listDados, String nomeRelatorio, Map<String, Object> params, ServletContext servletContext) throws Exception {
        //Cria a lista de Dados que vem por parametro:
        JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(listDados);

        //Caminho ficara sempre na pasta relatorio:
        String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";

        //Caminho do Relatorio, dados e gera a impressao:
        JasperPrint jasperPrint = JasperFillManager.fillReport(caminhoJasper, params, jRBeanCollectionDataSource);

        //Gerando para Excel:
        JRExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Parametro para deixar normalzinho excel, sem ficar sobreposto o rel sobre colunas e linhas.. fica feio!!! assim fica REIIIIIIIIIIIIIIIIII!!
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.exportReport();

        return baos.toByteArray();
    }
}
