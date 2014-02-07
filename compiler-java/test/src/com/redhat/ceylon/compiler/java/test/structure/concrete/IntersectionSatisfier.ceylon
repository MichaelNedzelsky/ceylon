/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
@noanno
interface IntersectionSatisfier_X<out T, out N> given N satisfies Null {
    shared formal T|N x;
}
@noanno
interface IntersectionSatisfier_I1<out U> satisfies IntersectionSatisfier_X<U, Null> {
    shared actual default U? x { return null; }
}
@noanno
interface IntersectionSatisfier_I2 satisfies IntersectionSatisfier_I1<Anything> {}
@noanno
class IntersectionSatisfier_C<V>() satisfies IntersectionSatisfier_I2&IntersectionSatisfier_I1<V> {}