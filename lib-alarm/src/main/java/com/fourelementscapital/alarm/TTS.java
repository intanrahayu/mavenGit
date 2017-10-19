/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Locale;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;


/**
 * A Text-To-Speech libraries using Free TTS & MBROLA voices 
 */
public class TTS {
	
	private SynthesizerModeDesc desc;
	private Synthesizer synthesizer;
	private Voice voice;
	
	public static final String VOICE_MBROLA_US1 = "mbrola_us1";
	
	public void init(String voiceName) throws EngineException, AudioException, EngineStateError, PropertyVetoException, IOException {
		
		//TODO : to properties file
		System.setProperty("mbrola.base", Config.getConfigValue_tts("mbrola_path"));
		
		if (desc == null) {
			System.setProperty("freetts.voices", "de.dfki.lt.freetts.en.us.MbrolaVoiceDirectory");
			desc = new SynthesizerModeDesc(Locale.US);
			Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
			synthesizer = Central.createSynthesizer(desc);
			synthesizer.allocate();
			synthesizer.resume();
			SynthesizerModeDesc smd = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
			Voice[] voices = smd.getVoices();
			Voice voice = null;
			for (int i = 0; i < voices.length; i++) {
				if (voices[i].getName().equals(voiceName)) {
					voice = voices[i];
					break;
				}
			}
			synthesizer.getSynthesizerProperties().setVoice(voice);
		}
	}
	
	public void terminate() throws EngineException, EngineStateError {
		synthesizer.deallocate();
	}	
	
	public void doSpeak(String speakText) throws EngineException, AudioException, IllegalArgumentException, InterruptedException {
		synthesizer.speakPlainText(speakText, null);
		synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
	}

	
}

 