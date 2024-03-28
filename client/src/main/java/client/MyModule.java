/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.ModelView.AdminLogInMv;
import client.ModelView.ContactInfoMv;
import client.ModelView.StartScreenMv;
import client.language.Translator;
import client.scenes.*;
import client.utils.ClientConfiguration;
import client.utils.RecentEventTracker;
import client.utils.communicators.implementations.*;
import client.utils.communicators.interfaces.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        configureScenes(binder);

        binder.bind(MenuBarCtrl.class).in(Scopes.SINGLETON);

        binder.bind(ClientConfiguration.class).in(Scopes.SINGLETON);
        binder.bind(Translator.class).in(Scopes.SINGLETON);
        binder.bind(RecentEventTracker.class).in(Scopes.SINGLETON);


        binder.bind(IEventCommunicator.class)
            .to(EventCommunicator.class).in(Scopes.SINGLETON);
        binder.bind(IParticipantCommunicator.class)
            .to(ParticipantCommunicator.class).in(Scopes.SINGLETON);
        binder.bind(IExpenseCommunicator.class)
            .to(ExpenseCommunicator.class).in(Scopes.SINGLETON);
        binder.bind(ITagCommunicator.class)
            .to(TagCommunicator.class).in(Scopes.SINGLETON);
        binder.bind(IParticipantCommunicator.class)
            .to(ParticipantCommunicator.class).in(Scopes.SINGLETON);
        binder.bind(IAdminCommunicator.class)
            .to(AdminCommunicator.class).in(Scopes.SINGLETON);

        try {
            binder.bind(StartScreenMv.class).toConstructor(
                StartScreenMv.class.getConstructor(
                    IEventCommunicator.class, RecentEventTracker.class))
                .in(Scopes.SINGLETON);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            binder.bind(ContactInfoMv.class).toConstructor(
                    ContactInfoMv.class.getConstructor(
                        IEventCommunicator.class, IParticipantCommunicator.class))
                .in(Scopes.SINGLETON);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            binder.bind(AdminLogInMv.class).toConstructor(
                    AdminLogInMv.class.getConstructor(
                        IAdminCommunicator.class))
                .in(Scopes.SINGLETON);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


    }

    private static void configureScenes(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddQuoteCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuoteOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddEditExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(InvitationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OpenDebtsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StatisticsScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ContactInfoCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TagScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminLogInCtrl.class).in(Scopes.SINGLETON);
    }
}