(ns pfparser.core
  
  (:use [clojure.string :only (split trim)])
  (:use [midje.sweet])
)




(def *sections* ["Defense" "Offense" "Tactics" "Statistics"])
(def *defense* ["AC" "hp" "Fort" "Ref" "Will" "Defensive Abilities"])
(def *offense* ["Speed" "Melee" "Ranged" "Special Attacks"])
(def *initial* ["Init" "Senses"])
(def *statistics* ["Str" "Dex" "Con" "Int" "Wis" "Cha" "Base Atk" "CMB" "CMD" "Feats" "Skills" "SQ" "Combat Gear" "Other Gear" "Special Abilities"])

(defn extract-title [text] (trim (first (split text #"CR"))))

(fact "Should extract char title"
  (extract-title (slurp "test/Sample2.txt")) => "Rival Faction Agents (4)")


(defn extract-CR [text] (Integer. (second (re-find #"CR (\d+)" text))))
(fact "Should extract char CR" 
  (extract-CR (slurp "test/Sample2.txt")) => (exactly 3))

(defn extract-XP [text] (Integer. (second (re-find #"XP (\d+)" text))))

(fact  "Should extract char XP"
  (extract-XP (slurp "test/Sample2.txt")) => (exactly 800))

(defn extract-race [text]
  (second (re-find #"([\p{Alpha}]+) [\p{Alpha}]+ \d+" text))) 
 

(fact "Should extract char race"
  (extract-race (slurp "test/Sample2.txt")) => "Human")

(defn extract-classes [text]
  (map (fn [c]{:class (first c) :level (Integer. (second c))}) (partition 2 (rest (re-find #"[\p{Alpha}]+ (?:(?:([\p{Alpha}]+) (\d+))/)+(?:([\p{Alpha}]+) (\d+))" text)))))

;.;. FAIL at (NO_SOURCE_FILE:1)
;.;.     Expected: ["fighter" "rogue"]
;.;.       Actual: (("fighter" "1") ("rogue" "3"))
;.;. 
;.;. FAIL at (NO_SOURCE_FILE:1)
;.;. Actual result did not agree with the checking function.
;.;.         Actual result: (nil nil)
;.;.     Checking function: (exactly [1 3])
(fact "Should extract char classes"
  (map :class (extract-classes (slurp "test/Sample2.txt"))) => ["fighter"  "rogue"]
  (map :level (extract-classes (slurp "test/Sample2.txt"))) => (exactly [1 3]))