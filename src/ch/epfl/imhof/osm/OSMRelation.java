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
        this.members = Collections.unmodifiableList(new ArrayList<>(
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

        public enum Type {
            NODE, WAY, RELATION
        }
    }

    public static final class Builder extends OSMEntity.Builder {
        private List<Member> members;

        public Builder(long id) {
            super(id);
            members = new ArrayList<>();
        }

        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }

        public OSMRelation build() throws IllegalStateException {
            if (isIncomplete()) throw new IllegalStateException("Cannot build Incomplete Relation");
            return new OSMRelation(id, members, b.build());
        }

    }
}
