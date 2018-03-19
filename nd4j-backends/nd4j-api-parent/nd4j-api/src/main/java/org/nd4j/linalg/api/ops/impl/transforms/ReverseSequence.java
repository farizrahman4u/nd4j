package org.nd4j.linalg.api.ops.impl.transforms;

import lombok.val;
import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.imports.NoOpNameFoundException;
import org.nd4j.imports.descriptors.properties.PropertyMapping;
import org.nd4j.imports.graphmapper.tf.TFGraphMapper;
import org.nd4j.linalg.api.ops.DynamicCustomOp;
import org.tensorflow.framework.AttrValue;
import org.tensorflow.framework.GraphDef;
import org.tensorflow.framework.NodeDef;

import java.util.*;


public class ReverseSequence extends DynamicCustomOp {


    int seqDim;
    int batchDim;



    public ReverseSequence(SameDiff sameDiff, SDVariable i_v, SDVariable seqLengths, int seqDim, int batchDim) {
        super(null, sameDiff, new SDVariable[]{i_v, seqLengths}, false);

        this.seqDim = seqDim;
        this.batchDim = batchDim;
        addArguments();

    }

    public ReverseSequence(SameDiff sameDiff, SDVariable i_v, SDVariable seqLengths) {
        super(null, sameDiff, new SDVariable[]{i_v, seqLengths}, false);
        this.seqDim = 1;
        this.batchDim = 0;
        addArguments();
    }

    private void addArguments(){
        addIArgument(seqDim);
        addIArgument(batchDim);
    }

    public ReverseSequence() {
    }

    @Override
    public String opName() {
        return "reverse_sequense";

    }

    @Override
    public void initFromTensorFlow(NodeDef nodeDef, SameDiff initWith, Map<String, AttrValue> attributesForNode, GraphDef graph) {
        TFGraphMapper.getInstance().initFunctionFromProperties(nodeDef.getOp(), this, attributesForNode, nodeDef, graph);
        addArguments();
    }

    @Override
    public Map<String, Map<String, PropertyMapping>> mappingsForFunction() {
        Map<String, Map<String, PropertyMapping>> ret = new HashMap<>();
        Map<String, PropertyMapping> attrs = new LinkedHashMap<>();
        val seqDim = PropertyMapping.builder()
                .propertyNames(new String[]{"seqDim"})
                .tfInputPosition(2)
                .build();
        val batchDim = PropertyMapping.builder()
                .propertyNames(new String[]{"batchDim"})
                .tfInputPosition(3)
                .build();
        attrs.put("seqDim", seqDim);
        attrs.put("batchDim", batchDim);
        ret.put(tensorflowName(), attrs);
        return ret;
    }

    @Override
    public String onnxName() {
        throw new NoOpNameFoundException("No onnx op opName found for " + opName());
    }

    @Override
    public String tensorflowName() {
        throw new NoOpNameFoundException("ReverseSequence");
    }


    @Override
    public List<SDVariable> doDiff(List<SDVariable> f1) {
        SDVariable ret = f().reverse_sequence(f1.get(0), f1.get(1), seqDim, batchDim);
        return Arrays.asList(ret);
    }

}
