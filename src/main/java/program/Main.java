package program;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import model.AF;
import model.Automaton;
import model.State;
import model.Transition;

//O programa possui apenas uma classe main
public class Main {

	private static CommandLine commandLine;

	public static void main(String[] args ) {
	
		receberEntradas(args);
		
		// Receber como entrada uma sentença e um arquivo no formato jflap contendo a descrição de um AFN qualquer
		String pathFile = Util.getOption('a', commandLine);
		Util.getOption('a', commandLine);
		String imput = Util.getOption('f', commandLine);
				 
		File fileAFN = new File(pathFile);
		AF afn = loadAF(fileAFN);
		
		// Gerar como saída outro arquivo contendo a descrição do AFD equivalente
		AF afd = convertToAFD(afn);
		afd.setType("fa");
		String output = pathFile.substring(0, pathFile.lastIndexOf("."))+"_convertidoAFD.jff";
		if(saveAF(afd, output ))
			System.out.println("Foi criado o arquivo "+ output);			
			
		// Simular AFD
		if(runAFD(afd, imput))
			System.out.println("Entrada "+imput+" ACEITA");
		else 
			System.out.println("Entrada "+imput+" NÃO ACEITA");
			
	}

	private static boolean runAFD(AF afd, String imput) {
		
		Automaton af = afd.getAutomaton();
		State stateCurrent = af.getInitialState();
		Transition transition = null;
		
		for (char symbol : imput.toCharArray()) {
			if(!af.getTransitionsFromStateBySymbol(stateCurrent, String.valueOf(symbol)).isEmpty()) {
				//Se existir transição para o simbolo o estado atual é alterado para o alvo da transição 
				transition = af.getTransitionsFromStateBySymbol(stateCurrent, String.valueOf(symbol)).get(0);
				stateCurrent = af.findStateById(transition.getTo());
			}	
			else {
				//Se não existir transição o automato não aceita a entrada. 
				return false;
			}		
		}
		
		//Quando a entrada for lida completamente é verificado se o estado atual é final.
		if(stateCurrent.getFinalState() != null)
			return true;
		else
			return false;
		
	}

	private static AF convertToAFD(AF afnDiagram) {
		Automaton afn = afnDiagram.getAutomaton();
		Automaton afd = new Automaton();
		
		//Adiciona estado inical no AFD
		afd.setState(new ArrayList<State>());
		afd.setTransition(new ArrayList<Transition>());
		afd.getState().add(afn.getInitialState());
		
		//Recupera alfabeto do automato
		List<String> alphabet = afn.getAlphabet();
		
		//Percorre todos os estados do AFD que vai ser construido
		int i = 0;
		do	{
			State stateAFD = afd.getState().get(i++);
			//Percorre todos os simbolos do alfabeto
			for (String symbol : alphabet) {
				
				//Recupera todas as transições do estados originais por simbolo
				List<String> originStateNames = Arrays.asList(stateAFD.getName().split(","));
				List<Transition> transitions = afn.getAllTransitionsFromStateBySymbol(originStateNames, symbol);
				
				//Se não existirem transições busca próximo estado
				if(transitions.isEmpty())
					break;
				
				//Cria novo estado concatenando os estados destinos 
				//Se um desses estados destino for final, o novo estado também será
				State newState = newTargetState(transitions, afn);
				String idNewState = Integer.toString(afd.lastIdofState()+1);
				newState.setId(idNewState);
				
				//Se esse estado não existir no AFD, adiciona-o
				if(afd.findStateByName(newState.getName()) == null)
					afd.getState().add(newState);
				else 
					newState = afd.findStateByName(newState.getName());
				
				//Adiciona uma transição para esse estado
				Transition newTransistion = new Transition();
				newTransistion.setFrom(stateAFD.getId());
				newTransistion.setTo(newState.getId());
				newTransistion.setRead(symbol);
				afd.getTransition().add(newTransistion);
			}
			
		}while(i<afd.getState().size());
		
		AF ret = new AF();
		ret.setAutomaton(afd);
		return ret;
	}
	
	private static State newTargetState(List<Transition> transitions, Automaton afn) {
		String newStateName = new String();
		boolean isStateFinal = false;
		for (Transition transition : transitions) {
			State targetState = afn.findStateById(transition.getTo());
			if (targetState.getFinalState() != null)
				isStateFinal = true;
			newStateName += targetState.getName() + ",";
		}
		newStateName = newStateName.substring(0, newStateName.length()-1);
					
		State newState = new State();
		newState.setName(newStateName);
		
		if(isStateFinal)
			newState.setFinalState("");
		
		return newState;
	}

	

	private static AF loadAF(File file) {
		AF afn = null; 
		try {

			JAXBContext context = JAXBContext.newInstance(AF.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			afn = (AF) unmarshaller.unmarshal(file);
		} catch (Exception exp) {
			System.out.println("Falha ao carregar FA");
		}
		
		return afn;
	}
	
	private static boolean saveAF(AF af, String output) {
		try {
			JAXBContext context = JAXBContext.newInstance(AF.class);
			Marshaller marshaller = context.createMarshaller();

			FileOutputStream fio = new FileOutputStream(output);

			marshaller.marshal(af, fio);

			fio.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			return false;
		}

		return true;
	}
	
	static void receberEntradas(String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		Option optA = OptionBuilder.hasArg().withArgName("-a").isRequired().withDescription("Caminho do arquivo Jflap")
				.create("a");
		Option optB = OptionBuilder.hasArg().withArgName("-f").isRequired()
				.withDescription("Fita de entrada do usuario").create("f");
	
		Options options = new Options();
		options.addOption( optA).addOption(optB);
		
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(2);
		}

	}

}
