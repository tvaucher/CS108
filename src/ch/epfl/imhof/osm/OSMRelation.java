package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;
import static java.util.Objects.requireNonNull;

public final class OSMRelation extends OSMEntity {
    private final List<Member> members;

    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<Member>(
                requireNonNull(members)));
    }

    public List<Member> members() {
        return members;
    }

    public static final class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;

        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        public Type type() {
            return type;
        }

        public String role() {
            return role;
        }

        public OSMEntity member() {
            return member;
        }

        public static final class Type {
            // TODO Do research and write this.
            // Enumeration of NODE, WAY, RELATION
            // We haven't learned about this yet. Will do a bit of research and
            // get back to it.
        }
    }
    
    public static final class Builder extends OSMEntity.Builder {
        private Member.Type type;
        private String role;
        private OSMEntity newMember;
        
        public Builder(long id) {
            super(id);
        }
        
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            // TODO after Type, write this.
        }
        
        // TODO Write OSMRelation build()
        
    }
}
