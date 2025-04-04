(ns bank-account)

(def bank-accounts
  "All accounts for the bank
  Each account has a unique id and a numeric value"
  (atom {}))

(defn open-account
  "Open an account with a zero balance."
  []
  (let [account-id (java.util.UUID/randomUUID)]
    (swap! bank-accounts assoc account-id 0)
    account-id))

(defn close-account [account-id]
  (swap! bank-accounts dissoc account-id))

(defn get-balance [account-id]
  (get @bank-accounts account-id))

(defn update-balance [account-id credit]
  (swap! bank-accounts update account-id #(+ % credit)))

(comment
  ;; Experimenting with code in the REPL
  (open-account)

  (= 0 (-> (open-account)
           (get-balance)))

  (deref bank-accounts)
   ;; => {#uuid "af0747fc-86bf-496a-8cc1-efb917887213" 0}

  (update-balance #uuid "af0747fc-86bf-496a-8cc1-efb917887213" 10)
  ;; => {#uuid "af0747fc-86bf-496a-8cc1-efb917887213" 10}

  (update-balance #uuid "af0747fc-86bf-496a-8cc1-efb917887213" -10))
  ;; => {#uuid "af0747fc-86bf-496a-8cc1-efb917887213" 0}
