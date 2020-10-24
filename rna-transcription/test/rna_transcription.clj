(ns rna-transtription-pure-test
  (:require [clojure.test :refer [deftest is]]
            [rna-transcription :as SUT]))


(deftest transcribes-cytosine-to-guanine
  (is (= "G" (SUT/dna->rna SUT/dna-nucleotide->rna-nucleotide "C"))))

(deftest transcribes-guanine-to-cytosine
  (is (= "C" (SUT/dna->rna SUT/dna-nucleotide->rna-nucleotide "G"))))

(deftest transcribes-adenine-to-uracil
  (is (= "U" (SUT/dna->rna SUT/dna-nucleotide->rna-nucleotide "A"))))

(deftest it-transcribes-thymine-to-adenine
  (is (= "A" (SUT/dna->rna SUT/dna-nucleotide->rna-nucleotide "T"))))

(deftest it-transcribes-all-nucleotides
  (is (= "UGCACCAGAAUU" (SUT/dna->rna SUT/dna-nucleotide->rna-nucleotide "ACGTGGTCTTAA"))))

(deftest it-validates-dna-strands
  (is (thrown? AssertionError (SUT/dna->rna SUT/dna-nucleotide->rna-nucleotide "XCGFGGTDTTAA"))))
