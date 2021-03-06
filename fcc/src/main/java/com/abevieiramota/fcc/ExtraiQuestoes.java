package com.abevieiramota.fcc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ExtraiQuestoes {

	private static String questaoRegex = "^\\d{1,2}\\.(.*?)\\(A\\)(.*?)\\(B\\)(.*?)\\(C\\)(.*?)\\(D\\)(.*?)\\(E\\)(.*?)(?=(\\d{1,2}\\.)|(Caderno\\ de\\ Prova))";
	private static Pattern questaoPattern = Pattern.compile(questaoRegex,
			Pattern.MULTILINE | Pattern.UNIX_LINES | Pattern.DOTALL);

	public List<Questao> extraiQuestoes(File fileProva) throws IOException {

		PDFTextStripper stripper = new PDFTextStripper();
		
		List<Questao> questoes = new ArrayList<Questao>();
		
		try (PDDocument doc = PDDocument.load(fileProva)) {
			
			String pdfAsText = stripper.getText(doc);

			Matcher m = questaoPattern.matcher(pdfAsText);

			while (m.find()) {

				Questao questao = new Questao();
				questao.setEnunciado(trataConteudo(m.group(1)));
				questao.setItem('A', trataConteudo(m.group(2)));
				questao.setItem('B', trataConteudo(m.group(3)));
				questao.setItem('C', trataConteudo(m.group(4)));
				questao.setItem('D', trataConteudo(m.group(5)));
				questao.setItem('E', trataConteudo(m.group(6)));

				questoes.add(questao);
			}
		}

		return questoes;
	}

	private String trataConteudo(String conteudo) {

		return conteudo.trim().replaceAll("(?<=\\d)o", "º").replaceAll("\\s{2,}", "\n");
	}

}
