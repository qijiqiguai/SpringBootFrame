package tech.qi.entity.support;


/**
 * JsonView hierarchy
 * Used to define jackson serialization
 *
 * @author wangqi
 */
public class View {

    public interface Common {
        public interface Summary {}
        public interface Detail extends Summary {}
    }

    public interface Admin {
        public interface Summary extends Common.Summary {}
        public interface Detail extends Common.Summary, Common.Detail {}
    }

}
