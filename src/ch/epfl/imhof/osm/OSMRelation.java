package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;
import static java.util.Objects.requireNonNull;

/**
 * Class representing Relations in the OSM model. Consists of a unique
 * identifier, a set of other OSMEntities (called members) and a lists of
 * attributes They can represents a wide range of "multipolygon" : e.g : a lake
 * with isles, a building with holes, ...
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public final class OSMRelation extends OSMEntity {
    private final List<Member> members;

    /**
     * Constructs a new OSMRelation based on given parameters
     * 
     * @param id
     *            unique identifier of the relation e.g RLC has id 331569
     * @see {@link http://www.openstreetmap.org/relation/331569}
     * @param members
     *            list of members of the relation
     * @see Member for more information on the topic
     * @param attributes
     *            list of attributes of the relation
     * @see Attributes for more information on the topic
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(members)));
    }

    /**
     * Returns the list of member of the current instance
     * 
     * @return members
     */
    public List<Member> members() {
        return members;
    }

    /**
     * Static sub-class of OSMRelation that represents a member of a OSMRelation
     * A member is composed of its type, role and associated OSMEntity
     */
    public static final class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;

        /**
         * Constructor of the sub-class Member based on given parameters
         * 
         * @param type
         *            Type of the member (type of entity)
         * @param role
         *            Role of the member should either be "inner" or "outer"
         * @param member
         *            entity given as member
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * Rreturns the type of the current instance (either NODE, WAY or
         * RELATION)
         * 
         * @return type
         */
        public Type type() {
            return type;
        }

        /**
         * Returns the role of the current instance (either "inner" or "outer")
         * 
         * @return role
         */
        public String role() {
            return role;
        }

        /**
         * Returns the member of the current instance
         * 
         * @return member
         */
        public OSMEntity member() {
            return member;
        }

        /**
         * Enumeration of the possible types of entity - node : @see
         * {@link OSMNode} - way : @see {@link OSMWay} - relation : @see
         * {@link OSMRelation}
         */
        public enum Type {
            NODE, WAY, RELATION
        }
    }

    /**
     * Builder associated to the OSMRelation class. Allows us to construct an
     * OSMRelation object step by step by adding members in the members list one
     * at a time.
     */
    public static final class Builder extends OSMEntity.Builder {
        private List<Member> members;

        /**
         * Constructor of the Builder, requires id of the OSMRelation that will
         * be built.
         * 
         * @param id
         *            unique identifier of the future relation
         */
        public Builder(long id) {
            super(id);
            members = new ArrayList<>();
        }

        /**
         * Adds a new Member into the list based on given parameters.
         * 
         * @param type
         *            Type of the new member
         * @param role
         *            Role of the new member
         * @param newMember
         *            OSMEntity that will be added: either a node, a way or a
         *            relation.
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }

        /**
         * Builds a new Relation object based on the data that has been inputted
         * into this builder.
         * 
         * @return the newly built OSMRelation
         * @throws IllegalStateException
         *             If the relation is incomplete
         */
        public OSMRelation build() throws IllegalStateException {
            if (isIncomplete())
                throw new IllegalStateException(
                        "Cannot build Incomplete Relation");
            return new OSMRelation(id, members, b.build());
        }

    }
}
