/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.util.List;

/**
 *
 * @author visat
 */
public interface Algorithm {
    public String getAlgorithmName();
    public String predict(List<String> attributes);
}
