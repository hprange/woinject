/**
 * Copyright (C) 2010 hprange <hprange@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.woinject.stubs;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFaultHandler;
import com.webobjects.eocontrol.EORelationshipManipulation;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class StubEnterpriseObject implements EOEnterpriseObject {
    @Inject
    @Named("test")
    private String injectedText;

    public void addObjectToBothSidesOfRelationshipWithKey(EORelationshipManipulation arg0, String arg1) {

    }

    public void addObjectToPropertyWithKey(Object arg0, String arg1) {

    }

    public NSArray<String> allPropertyKeys() {
        return null;
    }

    public NSArray<String> attributeKeys() {
        return null;
    }

    public void awakeFromClientUpdate(EOEditingContext arg0) {

    }

    public void awakeFromFetch(EOEditingContext arg0) {

    }

    public void awakeFromInsertion(EOEditingContext arg0) {

    }

    public NSDictionary changesFromSnapshot(NSDictionary<String, Object> arg0) {
        return null;
    }

    public EOClassDescription classDescription() {
        return null;
    }

    public EOClassDescription classDescriptionForDestinationKey(String arg0) {
        return null;
    }

    public void clearFault() {

    }

    public void clearProperties() {

    }

    public int deleteRuleForRelationshipKey(String arg0) {
        return 0;
    }

    public EOEditingContext editingContext() {
        return null;
    }

    public String entityName() {
        return null;
    }

    public String eoDescription() {
        return null;
    }

    public String eoShallowDescription() {
        return null;
    }

    public EOFaultHandler faultHandler() {
        return null;
    }

    public Object handleQueryWithUnboundKey(String paramString) {
        return null;
    }

    public void handleTakeValueForUnboundKey(Object paramObject, String paramString) {

    }

    public String inverseForRelationshipKey(String arg0) {
        return null;
    }

    public Object invokeRemoteMethod(String arg0, Class[] arg1, Object[] arg2) {
        return null;
    }

    public boolean isFault() {
        return false;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isToManyKey(String arg0) {
        return false;
    }

    public Object opaqueState() {
        return null;
    }

    public boolean ownsDestinationObjectsForRelationshipKey(String arg0) {
        return false;
    }

    public void prepareValuesForClient() {

    }

    public void propagateDeleteWithEditingContext(EOEditingContext arg0) {

    }

    public void reapplyChangesFromDictionary(NSDictionary<String, Object> arg0) {

    }

    public void removeObjectFromBothSidesOfRelationshipWithKey(EORelationshipManipulation arg0, String arg1) {

    }

    public void removeObjectFromPropertyWithKey(Object arg0, String arg1) {

    }

    public NSDictionary<String, Object> snapshot() {
        return null;
    }

    public Object storedValueForKey(String arg0) {
        return null;
    }

    public void takeStoredValueForKey(Object arg0, String arg1) {

    }

    public void takeValueForKey(Object paramObject, String paramString) {

    }

    public void takeValueForKeyPath(Object paramObject, String paramString) {

    }

    public void takeValuesFromDictionary(NSDictionary arg0) {
    }

    public void takeValuesFromDictionaryWithMapping(NSDictionary arg0, NSDictionary arg1) {
    }

    public NSArray<String> toManyRelationshipKeys() {
        return null;
    }

    public NSArray<String> toOneRelationshipKeys() {
        return null;
    }

    @Override
    public String toString() {
        return injectedText;
    }

    public void turnIntoFault(EOFaultHandler arg0) {

    }

    public void unableToSetNullForKey(String paramString) {

    }

    public void updateFromSnapshot(NSDictionary<String, Object> arg0) {

    }

    public String userPresentableDescription() {
        return null;
    }

    public void validateClientUpdate() throws ValidationException {

    }

    public void validateForDelete() throws ValidationException {

    }

    public void validateForInsert() throws ValidationException {

    }

    public void validateForSave() throws ValidationException {

    }

    public void validateForUpdate() throws ValidationException {

    }

    public Object validateTakeValueForKeyPath(Object paramObject, String paramString) throws ValidationException {
        return null;
    }

    public Object validateValueForKey(Object paramObject, String paramString) throws ValidationException {
        return null;
    }

    public Object valueForKey(String paramString) {
        return null;
    }

    public Object valueForKeyPath(String paramString) {
        return null;
    }

    public NSDictionary valuesForKeys(NSArray arg0) {
        return null;
    }

    public NSDictionary valuesForKeysWithMapping(NSDictionary arg0) {
        return null;
    }

    public void willChange() {

    }

    public void willRead() {

    }

    public Object willReadRelationship(Object arg0) {
        return null;
    }
}
