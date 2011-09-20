(ns pfparser.core
  
  (:use [clojure.string :only (split trim)])
  (:use [midje.sweet])
)



(def *alignments* "(LG|LN|LE|NG|N|NE|CG|CN|CE)")
(def *sizes* "(Tiny|Small|Medium|Large|Huge|Gargantuan|Colossal)")
(def *sections* ["Defense" "Offense" "Tactics" "Statistics"])
(def *defense* ["AC" "hp" "Fort" "Ref" "Will" "Defensive Abilities"])
(def *offense* ["Speed" "Melee" "Ranged" "Special Attacks"])
(def *initial* ["Init" "Senses"])
(def *statistics* ["Str" "Dex" "Con" "Int" "Wis" "Cha" "Base Atk" "CMB" "CMD" "Feats" "Skills" "SQ" "Combat Gear" "Other Gear" "Special Abilities"])
(defn extract-first [re text] (second (re-find re text)))
(defn extract-title [text] (trim (first (split text #"CR"))))

(fact "Should extract char title"
  (extract-title (slurp "test/Sample2.txt")) => "Rival Faction Agents (4)")


(defn extract-CR [text] (Integer. (extract-first #"CR (\d+)" text)))
(fact "Should extract char CR" 
  (extract-CR (slurp "test/Sample2.txt")) => (exactly 3))

(defn extract-XP [text] (Integer. (extract-first #"XP (\d+)" text)))

(fact  "Should extract char XP"
  (extract-XP (slurp "test/Sample2.txt")) => (exactly 800))

(defn extract-race [text]
  (extract-first #"([\p{Alpha}]+) [\p{Alpha}]+ \d+" text)) 
 

(fact "Should extract char race"
  (extract-race (slurp "test/Sample2.txt")) => "Human")

(defn extract-classes [text]
  (map (fn [c]{:class (first c) :level (Integer. (second c))}) (partition 2 (rest (re-find #"[\p{Alpha}]+ (?:(?:([\p{Alpha}]+) (\d+))/)+(?:([\p{Alpha}]+) (\d+))" text)))))

(fact "Should extract char classes"
  (map :class (extract-classes (slurp "test/Sample2.txt"))) => ["fighter"  "rogue"]
  (map :level (extract-classes (slurp "test/Sample2.txt"))) => (exactly [1 3]))


(defn extract-alignment [text]
  (extract-first (re-pattern *alignments*) text ))

(fact "Should extract char alignment"
  (extract-alignment (slurp "test/Sample2.txt")) => "CN")

(defn extract-size [text] (extract-first (re-pattern *sizes*) text ))

(fact "Should extract size"
  (extract-size (slurp "test/Sample2.txt")) => "Medium")