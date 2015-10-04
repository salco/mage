/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */

package mage.sets.lorwyn;

import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;

import mage.constants.CardType;
import mage.constants.Rarity;
import mage.abilities.costs.common.SacrificeTargetCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.continuous.BoostTargetEffect;
import mage.cards.CardImpl;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetControlledCreaturePermanent;
import mage.target.common.TargetCreaturePermanent;

/**
 * @author BursegSardaukar
 */
public class FodderLaunch extends CardImpl {

    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent("a Goblin");

    static {
        filter.add(new SubtypePredicate("Goblin"));
    }

    public FodderLaunch(UUID ownerId) {
        super(ownerId, 114, "Fodder Launch", Rarity.UNCOMMON, new CardType[]{CardType.SORCERY}, "{3}{B}");
        this.expansionSetCode = "LRW";
        this.subtype.add("Goblin");
        
        //As an additional cost to cast Fodder Launch, sacrifice a Goblin.
        this.getSpellAbility().addCost(new SacrificeTargetCost(new TargetControlledCreaturePermanent(1, 1, filter, false)));
        
        //Target creature gets -5/-5 until end of turn. Fodder Launch deals 5 damage to that creature's controller.
        this.getSpellAbility().addEffect(new BoostTargetEffect(-5, -5, Duration.EndOfTurn));
        this.getSpellAbility().addTarget(new TargetCreaturePermanent());
        this.getSpellAbility().addEffect(new FodderLaunchEffect());
    }

    public FodderLaunch(final FodderLaunch card) {
        super(card);
    }

    @Override
    public FodderLaunch copy() {
        return new FodderLaunch(this);
    }

}

class FodderLaunchEffect extends OneShotEffect {

    public FodderLaunchEffect() {
        super(Outcome.Damage);
        this.staticText = "Target creature gets -5/-5 until end of turn. Fodder Launch deals 5 damage to that creature's controller.";
    }

    public FodderLaunchEffect(final FodderLaunchEffect effect) {
        super(effect);
    }

    @Override
    public FodderLaunchEffect copy() {
        return new FodderLaunchEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (controller != null && sourceObject != null) {
            Permanent targetCreature = game.getPermanent(getTargetPointer().getFirst(game, source));
            if (targetCreature != null) {
                Player controllerOfTargetCreature = game.getPlayer(targetCreature.getControllerId());
                controllerOfTargetCreature.damage(5, source.getSourceId(), game, false, true);
                return true;
            }
        }   
        return false;
    }
}