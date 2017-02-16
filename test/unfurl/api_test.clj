;
; Copyright © 2016 Peter Monks
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
;

(ns unfurl.api-test
  (:require [midje.sweet :refer :all]
            [unfurl.api  :refer :all]))

(facts "Empty or invalid URLs"
  (unfurl nil)
    => nil
  (unfurl "")
    => (throws java.net.MalformedURLException)
  (unfurl "not a url")
    => (throws java.net.MalformedURLException)
  (unfurl "http://www.abcdefghijklmnopqrstuvwxyz.com/")
    => (throws java.net.UnknownHostException)
  (unfurl "https://localhost:8443/")
    => (throws java.net.ConnectException)
)

(facts "Incorrect content types"
  (unfurl "http://www.apache.org/licenses/LICENSE-2.0.txt")
    => nil
  (unfurl "http://www.ucolick.org/~diemand/vl/images/L800kpc_z0_0_poster.png")
    => nil
  (unfurl "http://samples.mplayerhq.hu/SWF/test.swf")
    => nil
)

(facts "Valid URLs"
  ; Simple HTML metatag-only site
  (unfurl "http://clojure.org/")
    => { :title "Clojure"
       }

  ; Site with HTML metatags plus (partial) OpenGraph tags
  (unfurl "http://www.facebook.com/")
    => { :url         "https://www.facebook.com/"
         :title       "Facebook - Log In or Sign Up"
         :description "Create an account or log into Facebook. Connect with friends, family and other people you know. Share photos and videos, send messages and get updates."
         :preview-url "https://www.facebook.com/images/fb_icon_325x325.png"
       }

  ; Everything and the kitchen sink tags (OpenGraph, Twitter, Swiftype and Sailthru!)
  (unfurl "https://techcrunch.com/2016/09/08/its-a-long-hard-road-from-idea-to-ipo/")
    => { :url         "http://social.techcrunch.com/2016/09/08/its-a-long-hard-road-from-idea-to-ipo/"
         :title       "It’s a long, hard road from idea to IPO"
         :description "It may not seem it, but coming up with an idea for your startup is probably the easiest part of launching your own company. As one industry insider told me,.."
         :preview-url "https://tctechcrunch2011.files.wordpress.com/2016/09/img_2835-1.jpg?w=764&h=400&crop=1"
       }
)
