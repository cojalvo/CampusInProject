package il.ac.shenkar.location.sound;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class RecorderThreadService
{
	private RealDoubleFFT transformer;
	private AudioRecord recorder;
	
	private short audioData[];
	private double toTransform[];
	
	private int freqThreshold = 16000; // Variable to set the minimum frequency to be filtered (Hertz)
	private double amplitudeThreshold = 3 ;  // variable to set the minimum volume of frequency range (Db spl)
	
	private int bufferSize;
	private int counter;
	private double ampSum;
	private double ampAvg;
	private static double currentFreq;  // Variable to hold the current frequency
	private boolean recording; // Variable to start or stop recording
	
	private Messenger messenger;
	
	public List<Double> getSoundCode()
	{
		List<Double>  retList=new ArrayList<Double>();
		double maxMagnitude;
		int maxIndex;
		
		System.out.println("Record Started");

		bufferSize = AudioRecord.getMinBufferSize(44100,
				AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT) * 3; // get the buffer size to use with this audio record

		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
				AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize); // instantiate the  AudioRecorder

		recording = true; // variable to use start or stop recording
		audioData = new short[bufferSize]; // short array that PCM data is put into.
		toTransform = new double[bufferSize];
		transformer = new RealDoubleFFT(bufferSize);
		for(int j=0;j<20;j++)
		{
			if(counter>50)
			{
				counter=0;
				ampSum=0;
			}
			
			maxIndex = -1;
			maxMagnitude = 0;
			
			if (recorder.getState() == android.media.AudioRecord.STATE_INITIALIZED) // check to see if the recorder has initialized yet.
				if (recorder.getRecordingState() == android.media.AudioRecord.RECORDSTATE_STOPPED)
					recorder.startRecording(); // check to see if the Recorder has stopped or is not recording, and make it record.
				else
				{
					int bufferReadResult = recorder.read(audioData, 0, bufferSize); // Read the PCM audio data into the audioData array

					for (int i = 0; i < bufferSize && i < bufferReadResult; i++)
                        toTransform[i] = (double) audioData[i] / 32768.0; // Signed 16 bit
					
					transformer.ft(toTransform);
					
					for (int i = 0; i < bufferSize/2-1; i++)
						if(i * 44100 / bufferSize > freqThreshold && (toTransform[i] > amplitudeThreshold)  )
						{
							// Increment the counter to calculate the amplitude average 
							counter++;
							
							// Setting the max amplitude peak variable  
							maxMagnitude = toTransform[i];
							
							ampSum += maxMagnitude;
							maxIndex = i;
							currentFreq = maxIndex * 44100 / bufferSize;
							ampAvg=ampSum/counter;
							
							//System.out.println("Freq: " + maxIndex * 44100 / bufferSize + " Amplitude RMS : " + ampAvg + "   Counter: " + counter  );
							
							Message msg = Message.obtain(); 
			                Bundle data = new Bundle();
			                
			                if(currentFreq>0) retList.add(Double.valueOf(currentFreq));
			                data.putDouble("freqResult", currentFreq);
			                data.putInt("ampResult", (int) ampAvg);
			                data.putInt("counter", counter);
			                msg.setData(data);
			            
						}
				}
			audioData = new short[bufferSize]; // short array that PCM data is put into.
			toTransform = new double[bufferSize];
			try
			{
				Thread.sleep(500); // Sleep 500ms before next record
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
			recording = false;
			if (recorder.getState() == android.media.AudioRecord.RECORDSTATE_RECORDING)
				recorder.stop(); // stop the recorder before ending the thread
			recorder.release(); // release the recorders resources
			recorder = null; // set the recorder to be garbage collected
			return retList;
	}
}