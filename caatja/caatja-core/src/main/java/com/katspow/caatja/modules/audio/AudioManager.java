package com.katspow.caatja.modules.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.katspow.caatja.modules.runtime.BrowserInfo;

/**
 * Sound implementation.
 */

/**
 * This class is a sound manager implementation which can play at least 'numChannels' sounds at the same time.
 * By default, CAAT.Director instances will set eight channels to play sound.
 * <p>
 * If more than 'numChannels' sounds want to be played at the same time the requests will be dropped,
 * so no more than 'numChannels' sounds can be concurrently played.
 * <p>
 * Available sounds to be played must be supplied to every CAAT.Director instance by calling <code>addSound</code>
 * method. The default implementation will accept a URL/URI or a HTMLAudioElement as source.
 * <p>
 * The cached elements can be played, or looped. The <code>loop</code> method will return a handler to
 * give the opportunity of cancelling the sound.
 * <p>
 * Be aware of Audio.canPlay, is able to return 'yes', 'no', 'maybe', ..., so anything different from
 * '' and 'no' will do.
 *
 * @constructor
 * 
 * FIXME ???
 *
 */
public class AudioManager {
    

    public AudioManager() {
        this.browserInfo = new BrowserInfo();
    }

    /**
     * The only background music audio channel.
     */
//    private AudioElement musicChannel;
    
    /**
     * Some browser info needed to know whether we�re in FF so we can fix the loop bug.
     */
    private BrowserInfo browserInfo;
    
    /**
     * Is music enabled ?
     */
    public boolean musicEnabled =       true;
    
    /**
     * Are FX sounds enabled ?
     */
    public boolean fxEnabled =          true;
    
    /**
     * A collection of Audio objects.
     */
//    private Map<String, AudioElement> audioCache; 
    
    /**
     * A cache of empty Audio objects.
     */
//    private Stack<AudioElement> channels;
    
    /**
     * Currently used Audio objects.
     */
//    private List<AudioElement> workingChannels;
    
    /**
     * Currently looping Audio objects.
     */
//    private List<AudioElement> loopingChannels =    new ArrayList<AudioElement>();
    
    // Add by me
    private AudioCallback caat_callback;
    
    /**
     * Audio formats.
     * @dict
     */
    // supported audio formats. Don"t remember where i took them from :S
    private Map<String, String> audioTypes = new HashMap<String, String>() {
        {
            put("mp3", "audio/mpeg;");
            put("ogg", "audio/ogg; codecs='vorbis'");
            put("wav", "audio/wav; codecs='1'");
            put("mp4", "audio/mp4; codecs='mp4a.40.2'");
        }
    };

    /**
     * Initializes the sound subsystem by creating a fixed number of Audio channels.
     * Every channel registers a handler for sound playing finalization. If a callback is set, the
     * callback function will be called with the associated sound id in the cache.
     *
     * @param numChannels {number} number of channels to pre-create. 8 by default.
     *
     * @return this.
     */
    public AudioManager initialize(int numChannels) {

//        this.audioCache = new HashMap<String, AudioElement>();
//        this.channels = new Stack<AudioElement>();
//        this.workingChannels = new ArrayList<AudioElement>();
//
//        for (int i = 0; i <= numChannels; i++) {
//            Audio audio = Audio.createIfSupported();
//            final AudioElement channel = audio.getAudioElement();
//
//            // FIXME what"s the point with this attribute ?
//            // channel.finished = -1;
//
//            this.channels.push(channel);
//
//            // TODO Check
//            audio.addEndedHandler(new EndedHandler() {
//                @Override
//                public void onEnded(EndedEvent audioEvent) {
//                    
//                    // remove from workingChannels
//                    workingChannels.remove(channel);
//                    
//                    // FIXME 
////                    if ( this.caat_callback != null) {
////                        this.caat_callback.call(this.caat_id);
////                    }
//
//                    // set back to channels.
//                    channels.push(channel);
//                }
//            });
//
//        }
//        
//        this.musicChannel = this.channels.pop();

        return this;
    }
    
    /**
     * Tries to add an audio tag to the available list of valid audios. The audio is described by a url.
     * @param id {object} an object to associate the audio element (if suitable to be played).
     * @param url {string} a string describing an url.
     * @param endplaying_callback {function} callback to be called upon sound end.
     *
     * @return {boolean} a boolean indicating whether the browser can play this resource.
     *
     * @private
     */
    public boolean addAudioFromURL(String id, String url, AudioCallback endplaying_callback ) {
        String extension= null;
////        Audio a = Audio.createIfSupported();
////        AudioElement audio = a.getAudioElement();
////
////        if ( null!=audio ) {
////
//////            if(!audio.canPlayType) {
//////                return false;
//////            }
////
////            extension= url.substring(url.lastIndexOf('.')+1);
////            String canplay= audio.canPlayType(this.audioTypes.get(extension));
////
////            if (!"".equals(canplay) && !"no".equals(canplay)) {
////                audio.setSrc(url);
////                audio.setPreload("auto");
////                audio.load();
////                
////                // FIXME
//////                if ( endplaying_callback != null) {
//////                    audio.caat_callback= endplaying_callback;
//////                    audio.caat_id= id;
//////                }
////                
////                this.audioCache.put(id, audio);
//
//                return true;
//            }
//        }

        return false;
    }
    
    /**
     * Tries to add an audio tag to the available list of valid audios. The audio element comes from
     * an HTMLAudioElement.
     * @param id {object} an object to associate the audio element (if suitable to be played).
     * @param audio {HTMLAudioElement} a DOM audio node.
     * @param endplaying_callback {function} callback to be called upon sound end.
     *
     * @return {boolean} a boolean indicating whether the browser can play this resource.
     *
     * @private
     */
//    public boolean addAudioFromDomNode(String id, AudioElement audio, AudioCallback endplaying_callback ) {
//
//        String extension= audio.getSrc().substring(audio.getSrc().lastIndexOf(".")+1);
//        
//        String canplay= audio.canPlayType(this.audioTypes.get(extension));
//        if (!"".equals(canplay) && !"no".equals(canplay)) {
//            
//            // FIXME
////            if ( endplaying_callback != null) {
////                audio.caat_callback= endplaying_callback;
////                audio.caat_id= id;
////            }
////            this.audioCache.put(id, audio);
//
//            return true;
//        }
//
//        return false;
//    }
    
    /**
     * Adds an elements to the audio cache.
     * @param id {object} an object to associate the audio element (if suitable to be played).
     * @param element {URL|HTMLElement} an url or html audio tag.
     * @param endplaying_callback {function} callback to be called upon sound end.
     *
     * @return {boolean} a boolean indicating whether the browser can play this resource.
     *
     * @private
     * 
     * TODO ???
     */
//    public boolean addAudioElement(String id, element, AudioCallback endplaying_callback ) {
//        if ( typeof element == "string" ) {
//            return this.addAudioFromURL( id, element, endplaying_callback );
//        } else {
//            try {
//                if ( element instanceof HTMLAudioElement ) {
//                    return this.addAudioFromDomNode( id, element, endplaying_callback );
//                }
//            }
//            catch(e) {
//            }
//        }
//
//        return false;
//    }

    /**
     * creates an Audio object and adds it to the audio cache.
     * This function expects audio data described by two elements, an id and an object which will
     * describe an audio element to be associated with the id. The object will be of the form
     * array, dom node or a url string.
     *
     * <p>
     * The audio element can be one of the two forms:
     *
     * <ol>
     *  <li>Either an HTMLAudioElement/Audio object or a string url.
     *  <li>An array of elements of the previous form.
     * </ol>
     *
     * <p>
     * When the audio attribute is an array, this function will iterate throught the array elements
     * until a suitable audio element to be played is found. When this is the case, the other array
     * elements won't be taken into account. The valid form of using this addAudio method will be:
     *
     * <p>
     * 1.<br>
     * addAudio( id, url } ). In this case, if the resource pointed by url is
     * not suitable to be played (i.e. a call to the Audio element's canPlayType method return 'no')
     * no resource will be added under such id, so no sound will be played when invoking the play(id)
     * method.
     * <p>
     * 2.<br>
     * addAudio( id, dom_audio_tag ). In this case, the same logic than previous case is applied, but
     * this time, the parameter url is expected to be an audio tag present in the html file.
     * <p>
     * 3.<br>
     * addAudio( id, [array_of_url_or_domaudiotag] ). In this case, the function tries to locate a valid
     * resource to be played in any of the elements contained in the array. The array element's can
     * be any type of case 1 and 2. As soon as a valid resource is found, it will be associated to the
     * id in the valid audio resources to be played list.
     *
     * @return this
     * 
     * TODO ????
     */
//    public AudioManager addAudio(String id, array_of_url_or_domnodes, AudioCallback endplaying_callback ) {
//
//        if ( array_of_url_or_domnodes instanceof Array ) {
//            /*
//             iterate throught array elements until we can safely add an audio element.
//             */
//            for( int i=0; i<array_of_url_or_domnodes.length; i++ ) {
//                if ( this.addAudioElement(id, array_of_url_or_domnodes[i], endplaying_callback) ) {
//                    break;
//                }
//            }
//        } else {
//            this.addAudioElement(id, array_of_url_or_domnodes, endplaying_callback);
//        }
//
//        return this;
//    }

    /**
     * Returns an audio object.
     * @param aId {object} the id associated to the target Audio object.
     * @return {object} the HTMLAudioElement addociated to the given id.
     */
//    public AudioElement getAudio(String aId) {
//        return audioCache.get(aId);
//    }
    
    public void stopMusic() {
//        this.musicChannel.pause();
    }

    
    public AudioManager playMusic(String id) {
        if (!this.musicEnabled) {
            return null;
        }

//        AudioElement audio_in_cache = this.getAudio(id);
//        // existe el audio, y ademas hay un canal de audio disponible.
//        if (null != audio_in_cache) {
//            AudioElement audio =this.musicChannel;
//            if (null != audio) {
//                audio.setSrc(audio_in_cache.getSrc());
//                audio.setPreload("auto");
//
//                if (this.browserInfo.browser.equals("Firefox")) {
//                      // TODO
////                    audio.addEventListener(
////                        "ended",
////                        // on sound end, restart music.
////                        function (audioEvent) {
////                            var target = audioEvent.target;
////                            target.currentTime = 0;
////                        },
////                        false
////                    );
//                } else {
//                    audio.setLoop(true);
//                }
//                audio.load();
//                audio.play();
//                
//                // TODO cannot return audioelement ...
//                return this;
//            }
//        }
        
        return null;
    }
    
    /**
     * Set an audio object volume.
     * @param id {object} an audio Id
     * @param volume {number} volume to set. The volume value is not checked.
     *
     * @return this
     */
    public AudioManager setVolume(String id, double volume ) {
//        AudioElement audio= this.getAudio(id);
//        if ( null!=audio ) {
//            audio.setVolume(volume);
//        }

        return this;
    }

    /**
     * Plays an audio file from the cache if any sound channel is available.
     * The playing sound will occupy a sound channel and when ends playing will leave
     * the channel free for any other sound to be played in.
     * @param id {object} an object identifying a sound in the sound cache.
     * @return { id: {Object}, audio: {(Audio|HTMLAudioElement)} }
     */
    public AudioManager play(String id) {
        if ( !this.fxEnabled ) {
            return null;
        }
        
//        AudioElement audio = this.getAudio(id);
//        // existe el audio, y ademas hay un canal de audio disponible.
//        if (null != audio && this.channels.size() > 0) {
//            AudioElement channel = this.channels.remove(0);
//            channel.setSrc(audio.getSrc());
////            channel.load();
//            channel.setVolume(audio.getVolume());
//            channel.play();
//            this.workingChannels.add(channel);
//        }

        // TODO Cannot return "audio"
        return this;
    }
    
    /**
     * cancel all instances of a sound identified by id. This id is the value set
     * to identify a sound.
     * @param id
     * @return {*}
     */
    public AudioManager cancelPlay(String id) {

//        for( int i=0 ; i < this.workingChannels.size(); i++ ) {
//            AudioElement audio = this.workingChannels.get(i);
//            // TODO
////            if ( audio.caat_id.equals(id)) {
////                audio.pause();
////                this.channels.add(audio);
////                this.workingChannels.splice(i,1);
////            }
//        }

        return this;
    }

    /**
     * cancel a channel sound
     * @param audioObject
     * @return {*}
     */
//    public AudioManager cancelPlayByChannel(AudioElement audioObject) {
////        this.workingChannels.remove(audioObject);
//////        for( var i=0 ; this.workingChannels.length; i++ ) {
//////            if ( this.workingChannels[i]===audioObject ) {
//////                this.channels.push(audioObject);
//////                this.workingChannels.splice(i,1);
//////                return this;
//////            }
//////        }
//
//        return this;
//    }

    /**
     * This method creates a new AudioChannel to loop the sound with.
     * It returns an Audio object so that the developer can cancel the sound loop at will.
     * The user must call <code>pause()</code> method to stop playing a loop.
     * <p>
     * Firefox does not honor the loop property, so looping is performed by attending end playing
     * event on audio elements.
     *
     * @return {HTMLElement} an Audio instance if a valid sound id is supplied. Null otherwise
     */
//    public AudioElement loop(String id) {
//        
//        if (!this.musicEnabled) {
//            return null;
//        }
//        
////        AudioElement audio_in_cache = this.getAudio(id);
////        // existe el audio, y ademas hay un canal de audio disponible.
////        if (null != audio_in_cache) {
////            Audio a = Audio.createIfSupported();
////
////            if (a != null) {
////                AudioElement audio = a.getAudioElement();
////                audio.setSrc(audio_in_cache.getSrc());
////                audio.setPreload("auto");
////                
////                if ( this.browserInfo.browser.equals("Firefox")) {
////                    
////                    // FIXME ...
//////                    audio.addEventListener(
//////                        "ended",
//////                        // on sound end, set channel to available channels list.
//////                        function(audioEvent) {
//////                            var target= audioEvent.target;
//////                            target.currentTime=0;
//////                        },
//////                        false
//////                    );
////                    
////                } else {
////                    audio.setLoop(true);
////                }
////                
////                audio.load();
////                audio.play();
////                this.loopingChannels.add(audio);
////                return audio;
////            }
////        }
//
//        return null;
//    }

    /**
     * Cancel all playing audio channels
     * Get back the playing channels to available channel list.
     *
     * @return this
     */
    public AudioManager endSound() {

//        for (AudioElement workingChannel : this.workingChannels) {
//            workingChannel.pause();
//            this.channels.push(workingChannel);
//        }
//        
//        for (AudioElement audioElement : this.loopingChannels) {
//            audioElement.pause();
//        }
//        
//        this.workingChannels.clear();
//        this.loopingChannels.clear();
//        
//        this.stopMusic();

        return this;
    }
    
    public AudioManager setSoundEffectsEnabled (boolean enable ) {
        this.fxEnabled= enable;
        
//        for (AudioElement audioElement : loopingChannels) {
//            if (enable) {
//                audioElement.play();
//            } else {
//                audioElement.pause();
//            }
//        }
        return this;
    }
    public boolean isSoundEffectsEnabled () {
        return this.fxEnabled;
    }
    
    public AudioManager setMusicEnabled (boolean enable ) {
        this.musicEnabled= enable;
        this.stopMusic();
        return this;
    }
    
    public boolean isMusicEnabled () {
        return this.musicEnabled;
    }

}
