;; ---------------------------------------------------------
;; Software transactional memory
;;
;; The atom
;; nothing to do with fission or fusion
;; - in Clojure it is a mutable container
;; - which contains a value (which will be immutable)
;; - and the value the atom contains can be changed safely
;; ---------------------------------------------------------

(ns design-journal)

;; ---------------------------------------------------------
;; Start with simple values

{:name "Johnny"}

(def me {:name "Johnny"})

me

(assoc me :age 42)

(atom {:name "Johnny"})

(def state (atom {:name "Johnny"}))

state

(deref state)
;; => {:name "Johnny"}

@state
;; => {:name "Johnny"}

;; swap!
(swap! state assoc :age 42)

(deref state)
;; => {:name "Johnny", :age 42}

;; reset!
(reset! state {:name "Johnny"})
;; => {:name "Johnny"}

(deref state)

;; assoc
;; assoc-in
;; update
;; update-in
;; defonce

(defonce state-reloaded (atom {}))

;; Examples
;; https://practicalli.github.io/clojurescript/reagent-projects/tic-tac-toe/reagent-design-manage-app-state.html
;; https://practicalli.github.io/clojurescript/reagent-projects/tic-tac-toe/update-game-board.html
;; https://practicalli.github.io/clojurescript/reagent-projects/tic-tac-toe/refactor--empty-cell.html

;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Creating a bank account

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns bank-account)

  (defonce bank-accounts (atom {}))

  (defn open-account []
    (let [account-id (java.util.UUID/randomUUID)]
      (swap! bank-accounts assoc account-id 0)
      account-id))

;; (dissoc {:name "me"} :name)

  (defn close-account [account-id]
    (swap! bank-accounts dissoc account-id))

  (defn get-balance [account-id]
    (get @bank-accounts account-id))

  (defn update-balance [account-id credit]
    (swap! bank-accounts update account-id #(+ % credit))))

   ;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

;; Using refs

  (ns bank-account)

  (defn open-account []
    (ref 0))

  (defn close-account [account]
    (dosync
     (ref-set account nil)))

  (defn get-balance [account]
    (dosync
     @account))

  (defn update-balance [account value]
    (dosync
     (alter account + value))))

   ;; End of rich comment block
;; ---------------------------------------------------------
