package org.grails.samples

/**
 * Simple domain object representing a visit.
 *
 * @author Graeme Rocher
 */
class Visit {

	Date date = new Date()
	String description
	Pet pet
    Vet vet

	static constraints = {
		description blank:false
	}
}
