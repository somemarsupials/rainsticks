(

/* SERVER CONFIG */

s = Server.local;

// output devices
s.options.outDevice_("Built-in Output");
s.options.numOutputBusChannels_(2);

// input devices
s.options.inDevice_("Built-in Microph");
s.options.numInputBusChannels_(2);

s.options.sampleRate_(44100);
s.options.memSize_(2.pow(20));
s.newBusAllocators;

ServerBoot.removeAll;
ServerTree.removeAll;
ServerQuit.removeAll;

/* INITIALISE GLOBAL VARIABLES */

~buffers = Dictionary.new;
~bus = Dictionary.new;
~out = 0;
~path = PathName(thisProcess.nowExecutingPath).parentPath;


/* PRELIMINARY FUNCTIONS */

~makeBuffers = {
  PathName(~path ++ "samples").files.do {
    arg sample;
    sample.fileName.asSymbol.postln;
    sample.fullPath.postln;

    ~buffers.add(
      sample.fileNameWithoutExtension.asSymbol ->
      Buffer.read(s, sample.fullPath)
    );
	};
};

~makeBusses = {
	~bus.add(\reverb -> Bus.audio(s, 2));
};

~cleanup = {
	s.newBusAllocators;
	ServerBoot.removeAll;
	ServerTree.removeAll;
	ServerQuit.removeAll;
};

/* REGISTER PRELIMINARY FUNCTIONS */

ServerBoot.add(~makeBuffers);
ServerBoot.add(~makeBusses);
ServerQuit.add(~cleanup);

s.waitForBoot({
  s.sync;

  // SYNTHDEFS TO GO HERE
});

)

(
~makeBuffers.value;
~buffers;